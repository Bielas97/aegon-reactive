package com.aegon.domain;

import lang.BaseException;

public class ApplicationUserPasswordException extends BaseException {

	private static final String PASSWORD_NOT_VALID_MSG = "Provided password is not valid";

	private ApplicationUserPasswordException(String msg) {
		super(msg);
	}

	public static ApplicationUserPasswordException notValid() {
		return new ApplicationUserPasswordException(PASSWORD_NOT_VALID_MSG);
	}
}
