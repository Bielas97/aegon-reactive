package com.aegon.application;

import com.aegon.DomainObject;
import com.aegon.domain.ApplicationUserId;
import com.aegon.domain.ApplicationUserImpl;
import com.aegon.domain.ApplicationUserPassword;
import com.aegon.domain.ApplicationUserRepository;
import com.aegon.domain.ApplicationUsername;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ApplicationUserPasswordService {

	private final ApplicationUserRepository userRepository;

	public Mono<ApplicationUserId> changePassword(ApplicationUserPassword newPassword) {
		return getCurrentUserUsername()
				.flatMap(username -> changePasswordOf(username, newPassword));
	}

	// TODO
	//		Cannot create a reference to an object with a NULL id.
	private Mono<ApplicationUserId> changePasswordOf(ApplicationUsername username, ApplicationUserPassword newPassword) {
		return userRepository.findByUsername(username)
				.flatMap(apiUser -> {
					final ApplicationUserImpl domainUserWithNewPassword = ((ApplicationUserImpl) apiUser).withNewPassword(newPassword);
					return userRepository.update(domainUserWithNewPassword).map(DomainObject::getId);
				});
	}

	private Mono<ApplicationUsername> getCurrentUserUsername() {
		return ReactiveSecurityContextHolder.getContext()
				.map(SecurityContext::getAuthentication)
				.map(auth -> ApplicationUsername.valueOf((String) auth.getPrincipal()));
	}

}
