package com.aegon.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
@RequiredArgsConstructor
public class SecurityResourceServer {

	private final SecurityResourceHandler securityResourceHandler;

	@Bean
	public RouterFunction<ServerResponse> securityRoutes() {
		return RouterFunctions.route(POST("/login"), securityResourceHandler::login)
				.andRoute(POST("/refresh-token"), securityResourceHandler::refreshToken)
				.andRoute(POST("/register"), securityResourceHandler::register)
				.andRoute(POST("/password"), securityResourceHandler::changePassword);
	}

}
