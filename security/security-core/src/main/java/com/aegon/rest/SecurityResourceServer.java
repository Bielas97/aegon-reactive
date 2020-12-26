package com.aegon.rest;

import com.aegon.domain.ApplicationUser;
import com.aegon.domain.ApplicationUserException;
import com.aegon.domain.ApplicationUserImpl;
import com.aegon.domain.ApplicationUserRepository;
import com.aegon.domain.JwtToken;
import com.aegon.infrastructure.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
@RequiredArgsConstructor
public class SecurityResourceServer {

	private final ApplicationUserRepository userRepository;

	private final JwtService jwtService;

	private final PasswordEncoder passwordEncoder;

	@Bean
	public RouterFunction<ServerResponse> securityRoutes() {
		final SecurityResourceHandler securityHandler = new SecurityResourceHandler(userRepository, jwtService, passwordEncoder);
		return RouterFunctions.route(POST("/login"), securityHandler::login);
	}

	@RequiredArgsConstructor
	private static class SecurityResourceHandler {

		private final ApplicationUserRepository userRepository;

		private final JwtService jwtService;

		private final PasswordEncoder passwordEncoder;

		private Mono<ServerResponse> login(ServerRequest request) {
			final Mono<TokenDTO> tokenMono = request.bodyToMono(SecurityCredentials.class)
					.flatMap(securityCredentials -> userRepository
							.findByUsername(securityCredentials.username)
							.flatMap(applicationUser -> createToken(securityCredentials, applicationUser)));
			return ServerResponse.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(BodyInserters.fromPublisher(tokenMono, TokenDTO.class));
		}

		private Mono<TokenDTO> createToken(SecurityCredentials securityCredentials, ApplicationUser applicationUser) {
			final ApplicationUserImpl userImpl = (ApplicationUserImpl) applicationUser;
			if (!passwordEncoder.matches(securityCredentials.password, userImpl.getPassword().getInternal())) {
				throw ApplicationUserException.wrongPassword();
			}
			final JwtToken domainToken = jwtService.createToken(userImpl);
			return Mono.just(domainToken.toDTO());
		}
	}

}