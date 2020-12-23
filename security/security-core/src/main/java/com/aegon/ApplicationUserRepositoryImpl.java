package com.aegon.infrastructure;

import com.aegon.domain.ApplicationUser;
import com.aegon.domain.ApplicationUserId;
import com.aegon.domain.ApplicationUserRepository;
import com.aegon.domain.MongoRoleDocument;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ApplicationUserRepositoryImpl implements ApplicationUserRepository {

	private final MongoUserRepository mongoUserRepository;

	@Override
	public Mono<ApplicationUser> findByUsername(String username) {
		return mongoUserRepository.findByUsername(username)
				.flatMap(a -> Mono.just(new ApplicationUser(
						new ApplicationUserId(a.getId()),
						a.getUsername(),
						a.getPassword(),
						a.getEmail(),
						a.getRoles().stream().map(MongoRoleDocument::getName).collect(Collectors.toSet()))));
	}
}
