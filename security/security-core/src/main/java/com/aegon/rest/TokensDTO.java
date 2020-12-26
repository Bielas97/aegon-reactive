package com.aegon.rest;

import com.aegon.domain.JwtToken;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TokensDTO {

	public String accessToken;

	public String refreshToken;

	public static TokensDTO of(JwtToken accessToken, JwtToken refreshToken) {
		return new TokensDTO(accessToken.getInternal(), refreshToken.getInternal());
	}

	public static TokensDTO of(JwtToken accessToken) {
		return new TokensDTO(accessToken.getInternal(), null);
	}

}
