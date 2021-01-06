package com.aegon.rest;

import com.aegon.application.ApplicationUserPasswordService;
import com.aegon.domain.ApplicationUser;
import com.aegon.domain.ApplicationUserException;
import com.aegon.domain.ApplicationUserId;
import com.aegon.domain.ApplicationUserImpl;
import com.aegon.domain.ApplicationUserRepository;
import com.aegon.domain.ApplicationUsername;
import com.aegon.domain.JwtToken;
import com.aegon.infrastructure.JwtService;
import java.util.LinkedList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SecurityResourceHandler {

	private final ApplicationUserRepository userRepository;

	private final JwtService jwtService;

	private final PasswordEncoder passwordEncoder;

	private final ApplicationUserPasswordService passwordService;

	public Mono<ServerResponse> login(ServerRequest request) {
		final Mono<TokensDTO> tokensMono = request.bodyToMono(SecurityCredentialsDTO.class)
				.flatMap(this::generateTokens);
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromPublisher(tokensMono, TokensDTO.class));
	}

	public Mono<ServerResponse> refreshToken(ServerRequest request) {
		final Mono<TokensDTO> tokensMono = request.bodyToMono(TokensDTO.class)
				.flatMap(this::generateTokens);
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromPublisher(tokensMono, TokensDTO.class));
	}

	public Mono<ServerResponse> register(ServerRequest request) {
		final Mono<UserDTO> result = request.bodyToMono(UserDTO.class)
				.flatMap(this::saveUser);
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromPublisher(result, UserDTO.class));
	}

	public Mono<ServerResponse> changePassword(ServerRequest request) {
		final Mono<ApplicationUserId> userId = request.bodyToMono(NewPasswordRequestDTO.class)
				.map(NewPasswordRequestDTO::toDomain)
				.flatMap(passwordService::changePassword);
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromPublisher(userId, ApplicationUserId.class));
	}

	private Mono<UserDTO> saveUser(UserDTO userDTO) {
		final var domainUser = userDTO.toDomain();
		return userRepository.save(domainUser)
				.map(user -> UserDTO.builder()
						.username(user.getUsername().getInternal())
						.email(user.getEmail().getInternal())
						.roles(new LinkedList<>(user.getRoles()))
						.build());
	}

	private Mono<TokensDTO> generateTokens(SecurityCredentialsDTO securityCredentialsDTO) {
		return userRepository.findByUsername(ApplicationUsername.valueOf(securityCredentialsDTO.username))
				.flatMap(applicationUser -> createToken(securityCredentialsDTO, applicationUser));
	}

	private Mono<TokensDTO> generateTokens(TokensDTO tokensDTO) {
		final JwtToken refreshToken = JwtToken.valueOf(tokensDTO.refreshToken);
		final Mono<JwtToken> accessToken = jwtService.createToken(refreshToken);
		return Mono.just(refreshToken)
				.zipWith(accessToken)
				.map(tuple -> TokensDTO.of(tuple.getT2(), tuple.getT1()));
	}

	private Mono<TokensDTO> createToken(SecurityCredentialsDTO securityCredentialsDTO, ApplicationUser applicationUser) {
		final ApplicationUserImpl userImpl = (ApplicationUserImpl) applicationUser;
		if (!passwordEncoder.matches(securityCredentialsDTO.password, userImpl.getPassword().getInternal())) {
			throw ApplicationUserException.wrongPassword();
		}
		final Mono<JwtToken> accessToken = jwtService.createToken(userImpl);
		final Mono<JwtToken> refreshToken = jwtService.createRefreshToken(userImpl);

		return accessToken
				.zipWith(refreshToken)
				.map(tuple -> TokensDTO.of(tuple.getT1(), tuple.getT2()));
	}
}
