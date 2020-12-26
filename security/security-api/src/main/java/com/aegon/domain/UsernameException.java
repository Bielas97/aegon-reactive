package com.aegon.domain;

import lang.BaseException;

public class UsernameException extends BaseException {

	private static final String USERNAME_NOT_VALID_MSG = "Provided username is not valid";

	private UsernameException(String msg) {
		super(msg);
	}

	public static UsernameException notValid() {
		return new UsernameException(USERNAME_NOT_VALID_MSG);
	}
}
