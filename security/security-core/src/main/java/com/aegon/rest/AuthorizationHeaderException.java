package com.aegon.rest;

import lang.BaseException;

public class AuthorizationHeaderException extends BaseException {

	private static final String WRONG_FORMAT_MSG = "Authorization header is wrongly formatted";

	private AuthorizationHeaderException(String msg) {
		super(msg);
	}

	public static AuthorizationHeaderException wrongFormat(){
		return new AuthorizationHeaderException(WRONG_FORMAT_MSG);
	}
}
