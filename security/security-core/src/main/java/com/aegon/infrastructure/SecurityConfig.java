package com.aegon.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationManager jwtAuthenticationManager;

	private final SecurityContextRepository securityContextRepository;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public SecurityWebFilterChain springSecurityWebFilterChain(ServerHttpSecurity http) {
		return http.authorizeExchange()

				// ============ Allow /login route ============
				// TODO
				//		in deployment allow register route only to ROLE_USERS_ADMIN role
				.pathMatchers("/login", "/refresh-token", "/register")
				.permitAll()

				// ============ Authenticate any other routes ============
				.anyExchange()
				.authenticated()
				.and()

				// ============ Filters ============
				.addFilterAt(new AuthenticationWebFilter(jwtAuthenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
				.securityContextRepository(securityContextRepository)

				// ============ Exceptions ============
				.exceptionHandling()
				.authenticationEntryPoint((exchange, e) -> Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
				.accessDeniedHandler((exchange, e) -> Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN)))
				.and()

				// ============ Disable form/csrf/login/logout ============
				.httpBasic()
				.disable()
				.csrf()
				.disable()
				.formLogin()
				.disable()
				.logout()
				.disable()
				.build();
	}

}
