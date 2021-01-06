package com.aegon.domain;

import com.aegon.util.lang.DomainReactiveRepository;
import reactor.core.publisher.Mono;

//@AdminSecured
public interface ApplicationUserRepository extends DomainReactiveRepository<ApplicationUserId, ApplicationUser> {

	Mono<ApplicationUser> findByUsername(ApplicationUsername username);

}
