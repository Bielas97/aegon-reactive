package com.aegon.infrastructure;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;

public class AuthorizationHeaderException extends BaseException {

	private static final String WRONG_FORMAT_MSG = "Authorization header is wrongly formatted";

	private AuthorizationHeaderException(String msg) {
		super(new ExceptionError(msg));
	}

	static AuthorizationHeaderException wrongFormat(){
		return new AuthorizationHeaderException(WRONG_FORMAT_MSG);
	}
}
