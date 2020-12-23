package com.aegon.infrastructure;

import com.aegon.domain.ApplicationUserDetails;
import com.aegon.domain.ApplicationUserRepository;
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
		return userRepository.findByUsername(username)
				.flatMap(user -> Mono.just(ApplicationUserDetails.from(user)));
	}
}