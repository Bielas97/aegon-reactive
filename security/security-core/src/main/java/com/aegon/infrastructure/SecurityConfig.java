package com.aegon.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public MapReactiveUserDetailsService userDetailsService() {
		return null;
	}

	@Bean
	public SecurityWebFilterChain springSecurityWebFilterChain(ServerHttpSecurity http) {
		return http
				.authorizeExchange()
				.anyExchange().authenticated()
				.and()
				.httpBasic()
				.and()
				.formLogin()
				.and()
				.build();
	}

}
