package com.aegon.domain;

import lang.BaseException;

public class ApplicationUserException extends BaseException {

	private static final String USER_NOT_FOUND_MSG = "Application user not found!";

	private static final String WRONG_PASSWORD_MSG = "Wrong password!";

	private ApplicationUserException(String msg) {
		super(msg);
	}

	public static ApplicationUserException notFound() {
		return new ApplicationUserException(USER_NOT_FOUND_MSG);
	}

	public static ApplicationUserException wrongPassword() {
		return new ApplicationUserException(WRONG_PASSWORD_MSG);
	}

}
