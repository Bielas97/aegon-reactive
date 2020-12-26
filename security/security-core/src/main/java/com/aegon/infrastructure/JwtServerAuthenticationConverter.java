package com.aegon.infrastructure;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {

	@Override
	public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
		return Mono.justOrEmpty(serverWebExchange)
				.flatMap(exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION)))
				.map(header -> new UsernamePasswordAuthenticationToken(header, header));
	}
}