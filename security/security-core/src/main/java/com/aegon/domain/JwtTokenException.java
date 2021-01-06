package com.aegon.domain;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;

public class JwtTokenException extends BaseException {

	private static final String EXPIRED_MSG = "Token has expired!";

	private JwtTokenException(String msg) {
		super(new ExceptionError(msg));
	}

	public static JwtTokenException expired() {
		return new JwtTokenException(EXPIRED_MSG);
	}

	public static JwtTokenException with(String msg) {
		return new JwtTokenException(msg);
	}
}
