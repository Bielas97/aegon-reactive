package com.aegon.infrastructure;

import com.aegon.BaseException;

public class AuthorizationHeaderException extends BaseException {

	private static final String WRONG_FORMAT_MSG = "Authorization header is wrongly formatted";

	private AuthorizationHeaderException(String msg) {
		super(msg);
	}

	static AuthorizationHeaderException wrongFormat(){
		return new AuthorizationHeaderException(WRONG_FORMAT_MSG);
	}
}
