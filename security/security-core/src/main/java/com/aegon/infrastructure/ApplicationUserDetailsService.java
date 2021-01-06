package com.aegon.infrastructure;

import com.aegon.domain.ApplicationUserException;
import com.aegon.domain.ApplicationUserImpl;
import com.aegon.domain.ApplicationUserRepository;
import com.aegon.domain.ApplicationUsername;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ApplicationUserDetailsService implements ReactiveUserDetailsService {

	private final ApplicationUserRepository userRepository;

	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return userRepository.findByUsername(ApplicationUsername.valueOf(username))
				.map(user -> ((ApplicationUserImpl) user).toUserDetails())
				.switchIfEmpty(Mono.error(ApplicationUserException::notFound));
	}
}
