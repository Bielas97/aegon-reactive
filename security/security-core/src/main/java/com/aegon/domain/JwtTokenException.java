package com.aegon.domain;

import com.aegon.BaseException;

public class JwtTokenException extends BaseException {

	private static final String EXPIRED_MSG = "Token has expired!";

	private JwtTokenException(String msg) {
		super(msg);
	}

	public static JwtTokenException expired() {
		return new JwtTokenException(EXPIRED_MSG);
	}
}
