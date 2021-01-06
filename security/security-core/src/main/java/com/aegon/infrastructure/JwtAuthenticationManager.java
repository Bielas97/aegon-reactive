package com.aegon.infrastructure;

import com.aegon.domain.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

	private final JwtService jwtService;

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		return Mono.just(authentication)
				.map(Authentication::getCredentials)
				.map(obj -> jwtService.parseToken((JwtToken) obj))
				.onErrorResume(e -> Mono.empty())
				.map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
						null,
						userDetails.getAuthorities())
				);
	}
}
