package com.aegon.domain;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;

public class ApplicationUserException extends BaseException {

	private static final String USER_NOT_FOUND_MSG = "Application user not found!";

	private static final String WRONG_PASSWORD_MSG = "Wrong password!";

	private ApplicationUserException(String msg) {
		super(new ExceptionError(msg));
	}

	public static ApplicationUserException notFound() {
		return new ApplicationUserException(USER_NOT_FOUND_MSG);
	}

	public static ApplicationUserException wrongPassword() {
		return new ApplicationUserException(WRONG_PASSWORD_MSG);
	}

}
