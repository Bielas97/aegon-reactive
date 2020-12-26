package com.aegon.infrastructure;

import com.aegon.rest.AuthorizationHeaderException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {

	private final JwtAuthenticationManager jwtAuthenticationManager;

	@Override
	public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
		throw new UnsupportedOperationException("Saving session is not supported!");
	}

	@Override
	public Mono<SecurityContext> load(ServerWebExchange exchange) {
		final ServerHttpRequest request = exchange.getRequest();
		final String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			final String authToken = authHeader.substring(7);
			final Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
			return jwtAuthenticationManager
					.authenticate(auth)
					.map(SecurityContextImpl::new);
		} else {
			return Mono.error(AuthorizationHeaderException.wrongFormat());
		}
	}
}
