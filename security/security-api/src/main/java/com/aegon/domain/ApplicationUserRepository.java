package com.aegon.domain;

import com.aegon.DomainRepository;
import reactor.core.publisher.Mono;

//@AdminSecured
public interface ApplicationUserRepository extends DomainRepository<ApplicationUserId, ApplicationUser> {

	Mono<ApplicationUser> findByUsername(ApplicationUsername username);

}
