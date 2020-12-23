package com.aegon.domain;

import reactor.core.publisher.Mono;

public interface ApplicationUserRepository {

	Mono<ApplicationUser> findByUsername(String username);

}
