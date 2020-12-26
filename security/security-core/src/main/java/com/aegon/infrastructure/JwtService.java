package com.aegon.infrastructure;

import com.aegon.domain.ApplicationUserImpl;
import com.aegon.domain.ApplicationUserRole;
import com.aegon.domain.ApplicationUsername;
import com.aegon.domain.JwtToken;
import com.aegon.domain.JwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import lang.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	private static final String TOKEN_PREFIX = "BEARER ";

	private static final String EMAIL_CLAIM = "email";

	private static final String ROLES_CLAIM = "roles";

	@Value("${security.secret-key}")
	private String secretKey;

	@Value("${security.access-token-expiration-time}")
	private String accessTokenExpirationTime;

	@Value("${security.refresh-token-expiration-time}")
	private String refreshTokenExpirationTime;

	public JwtToken createToken(ApplicationUserImpl user) {
		return createToken(user, Long.parseLong(accessTokenExpirationTime));
	}

	public JwtToken createToken(JwtToken refreshToken) {
		final ApplicationUserImpl applicationUser = parseToken(refreshToken.getInternal());
		return createToken(applicationUser, Long.parseLong(accessTokenExpirationTime));
	}

	public JwtToken createRefreshToken(ApplicationUserImpl user) {
		return createToken(user, Long.valueOf(refreshTokenExpirationTime));
	}

	private JwtToken createToken(ApplicationUserImpl user, Long expirationTime) {
		final String roles = joinRoles(user);
		return JwtToken.valueOf(
				Jwts.builder()
						.setSubject(user.getUsername().getInternal())
						.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
						.setIssuedAt(new Date(System.currentTimeMillis()))
						.signWith(SignatureAlgorithm.HS512, secretKey)
						.claim(EMAIL_CLAIM, user.getEmail().getInternal())
						.claim(ROLES_CLAIM, roles)
						.compact()
		);
	}

	public UserDetails parseToken(JwtToken token) {
		final ApplicationUserImpl applicationUser = parseToken(token.getInternal());
		return applicationUser.toUserDetails();
	}

	private ApplicationUserImpl parseToken(String rawToken) {
		final Claims claims = getClaims(rawToken);
		validateClaims(claims);
		final String username = claims.getSubject();
		final String email = claims.get(EMAIL_CLAIM).toString();
		final Set<ApplicationUserRole> roles = Arrays.stream(claims.get(ROLES_CLAIM).toString().split(","))
				.map(ApplicationUserRole::valueOf)
				.collect(Collectors.toSet());
		return new ApplicationUserImpl(ApplicationUsername.valueOf(username),
				Email.valueOf(email),
				roles);
	}

	private String joinRoles(ApplicationUserImpl user) {
		return user.getRoles()
				.stream()
				.map(Enum::name)
				.collect(Collectors.joining(","));
	}

	private Claims getClaims(String rawToken) {
		return Jwts
				.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(rawToken.replace(TOKEN_PREFIX, ""))
				.getBody();
	}

	private void validateClaims(Claims claims) {
		final Date expiration = claims.getExpiration();
		if (expiration.before(new Date())) {
			throw JwtTokenException.expired();
		}
		final String username = claims.getSubject();
		final String email = claims.get(EMAIL_CLAIM).toString();
		final Set<ApplicationUserRole> roles = Arrays.stream(claims.get(ROLES_CLAIM).toString().split(","))
				.map(ApplicationUserRole::valueOf)
				.collect(Collectors.toSet());
		if (username == null || username.isEmpty() || email == null || email.isEmpty() || roles.isEmpty()) {
			throw JwtTokenException.with("Claims are not valid");
		}
	}

}
