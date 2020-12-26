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

	@Value("${security.expiration-time}")
	private String expirationTime;

	public JwtToken createToken(ApplicationUserImpl user) {
		final String roles = user.getRoles()
				.stream()
				.map(Enum::name)
				.collect(Collectors.joining(","));
		return JwtToken.valueOf(
				Jwts.builder()
						.setSubject(user.getUsername().getInternal())
						.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(expirationTime)))
						.setIssuedAt(new Date(System.currentTimeMillis()))
						.signWith(SignatureAlgorithm.HS512, secretKey)
						.claim(EMAIL_CLAIM, user.getEmail().getInternal())
						.claim(ROLES_CLAIM, roles)
						.compact()
		);
	}

	public UserDetails parseToken(JwtToken token) {
		final Claims claims = Jwts
				.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token.getInternal().replace(TOKEN_PREFIX, ""))
				.getBody();
		final String username = claims.getSubject();
		final String email = claims.get(EMAIL_CLAIM).toString();
		final Set<ApplicationUserRole> roles = Arrays.stream(claims.get(ROLES_CLAIM).toString().split(","))
				.map(ApplicationUserRole::valueOf)
				.collect(Collectors.toSet());
		validateClaims(username, email, roles);
		final ApplicationUserImpl applicationUser = new ApplicationUserImpl(ApplicationUsername.valueOf(username),
				Email.valueOf(email),
				roles);
		return applicationUser.toUserDetails();
	}

	private void validateClaims(String username, String email, Set<ApplicationUserRole> roles) {
		if (username == null || username.isEmpty() || email == null || email.isEmpty() || roles.isEmpty()) {
			throw JwtTokenException.with("Claims are not valid");
		}
	}

}
