package com.aegon.application;

import com.aegon.domain.ApplicationUser;
import com.aegon.domain.ApplicationUserId;
import com.aegon.domain.ApplicationUserImpl;
import com.aegon.domain.ApplicationUserRepository;
import com.aegon.domain.ApplicationUsername;
import com.aegon.domain.MongoRoleDocument;
import com.aegon.domain.MongoUserDocument;
import com.aegon.infrastructure.MongoRoleRepository;
import com.aegon.infrastructure.MongoUserRepository;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ApplicationUserRepositoryImpl implements ApplicationUserRepository {

	private final MongoUserRepository mongoUserRepository;

	private final MongoRoleRepository mongoRoleRepository;

	private final PasswordEncoder passwordEncoder;

	@Override
	public Mono<ApplicationUser> findByUsername(ApplicationUsername username) {
		return mongoUserRepository.findByUsername(username.getInternal())
				.flatMap(userDocument -> Mono.just(ApplicationUserImpl.from(userDocument)));
	}

	@Override
	public Mono<ApplicationUser> findById(ApplicationUserId id) {
		return mongoUserRepository.findById(id.getInternal())
				.map(ApplicationUserImpl::from);
	}

	@Override
	public Mono<ApplicationUser> save(ApplicationUser user) {
		return Mono.just(user).flatMap(this::saveApiUserWithRoles);
	}

	@Override
	public Mono<ApplicationUserId> delete(ApplicationUserId objectId) {
		return mongoUserRepository.deleteById(objectId.getInternal()).flatMap(d -> Mono.just(objectId));
	}

	@Override
	public Mono<ApplicationUser> update(ApplicationUser updated) {
		return Mono.just(updated).flatMap(this::updateApiUser);
	}

	private Mono<ApplicationUser> saveApiUserWithRoles(ApplicationUser apiUser) {
		final ApplicationUserImpl domainUser = (ApplicationUserImpl) apiUser;
		return saveRoles(domainUser)
				.collectList()
				.flatMap(roles -> saveDomainUserWithRolesAttached(domainUser, roles));
	}

	private Flux<MongoRoleDocument> saveRoles(ApplicationUserImpl domainUser) {
		return Flux.fromIterable(domainUser.getMongoRoles())
				.flatMap(mongoRole -> mongoRoleRepository.findByName(mongoRole.getName())
						.switchIfEmpty(Mono.defer(() -> mongoRoleRepository.save(mongoRole))));
	}

	private Mono<ApplicationUserImpl> saveDomainUserWithRolesAttached(ApplicationUserImpl domainUser, List<MongoRoleDocument> roles) {
		var encodedPassword = passwordEncoder.encode(domainUser.getPassword().getInternal());
		final MongoUserDocument mongoUser = MongoUserDocument.builder()
				.username(domainUser.getUsername().getInternal())
				.email(domainUser.getEmail().getInternal())
				.password(encodedPassword)
				.roles(new HashSet<>(roles))
				.build();
		return mongoUserRepository
				.save(mongoUser)
				.map(ApplicationUserImpl::from);
	}

	private Mono<ApplicationUserImpl> updateApiUser(ApplicationUser apiUser) {
		return Flux.fromIterable(apiUser.getRoles())
				.flatMap(mongoRoleRepository::findByName)
				.collectList()
				.flatMap(mongoRoles -> {
					final var domainUser = (ApplicationUserImpl) apiUser;
					final var encodedPassword = passwordEncoder.encode(domainUser.getPassword().getInternal());
					final MongoUserDocument mongoUser = MongoUserDocument.builder()
							.id(domainUser.getId().getInternal())
							.username(domainUser.getUsername().getInternal())
							.email(domainUser.getEmail().getInternal())
							.password(encodedPassword)
							.roles(new HashSet<>(mongoRoles))
							.build();
					return mongoUserRepository
							.save(mongoUser)
							.map(ApplicationUserImpl::from);
				});
	}
}
