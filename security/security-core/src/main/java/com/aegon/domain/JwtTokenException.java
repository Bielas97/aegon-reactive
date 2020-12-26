package com.aegon.domain;

import lang.BaseException;

public class JwtTokenException extends BaseException {

	private JwtTokenException(String msg) {
		super(msg);
	}
}
