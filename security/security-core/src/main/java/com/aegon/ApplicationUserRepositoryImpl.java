package com.aegon;

import com.aegon.domain.ApplicationUser;
import com.aegon.domain.ApplicationUserId;
import com.aegon.domain.ApplicationUserImpl;
import com.aegon.domain.ApplicationUserPassword;
import com.aegon.domain.ApplicationUserRepository;
import com.aegon.domain.ApplicationUsername;
import com.aegon.domain.MongoRoleDocument;
import com.aegon.domain.MongoUserDocument;
import java.util.Set;
import java.util.stream.Collectors;
import lang.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ApplicationUserRepositoryImpl implements ApplicationUserRepository {

	private final MongoUserRepository mongoUserRepository;

	private final MongoRoleRepository mongoRoleRepository;

	private final PasswordEncoder passwordEncoder;

	@Override
	public Mono<ApplicationUser> findByUsername(String username) {
		return mongoUserRepository.findByUsername(username)
				.flatMap(userDocument -> Mono.just(createApplicationUser(userDocument)));
	}

	@Override
	public Mono<ApplicationUser> save(ApplicationUser user) {
		final ApplicationUserImpl userImpl = (ApplicationUserImpl) user;
		final String encodedPassword = passwordEncoder.encode(userImpl.getPassword().getInternal());
		final MongoUserDocument mongoUserDocument = createMongoUser(userImpl, encodedPassword);
		final Mono<MongoUserDocument> result = mongoUserRepository.save(mongoUserDocument);
		return result.flatMap(userDocument -> Mono.just(createApplicationUser(userDocument)));
	}

	private ApplicationUserImpl createApplicationUser(MongoUserDocument userDocument) {
		return new ApplicationUserImpl(ApplicationUserId.valueOf(userDocument.getId()),
				ApplicationUsername.valueOf(userDocument.getUsername()),
				Email.valueOf(userDocument.getEmail()),
				ApplicationUserPassword.valueOf(userDocument.getPassword()),
				userDocument.getRoles().stream().map(MongoRoleDocument::getName).collect(Collectors.toSet()));
	}

	private MongoUserDocument createMongoUser(ApplicationUserImpl userImpl, String encodedPassword) {
		final Set<MongoRoleDocument> roles = extractRoles(userImpl);
		return new MongoUserDocument(userImpl.getUsername().getInternal(),
				userImpl.getEmail().getInternal(),
				encodedPassword,
				roles);
	}

	private Set<MongoRoleDocument> extractRoles(ApplicationUserImpl userImpl) {
		return userImpl.getRoles()
				.stream()
				.map(this::saveUserRole)
				.collect(Collectors.toSet());
	}

	private MongoRoleDocument saveUserRole(com.aegon.domain.ApplicationUserRole role) {
		final MongoRoleDocument block = mongoRoleRepository.findByName(role).block();
		if (block == null) {
			return mongoRoleRepository.save(new MongoRoleDocument(role)).block();
		}
		return block;
	}
}
