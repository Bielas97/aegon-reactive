package com.aegon.domain;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;

public class ApplicationUserPasswordException extends BaseException {

	private static final String PASSWORD_NOT_VALID_MSG = "Provided password is not valid";

	private ApplicationUserPasswordException(String msg) {
		super(new ExceptionError(msg));
	}

	public static ApplicationUserPasswordException notValid() {
		return new ApplicationUserPasswordException(PASSWORD_NOT_VALID_MSG);
	}
}
