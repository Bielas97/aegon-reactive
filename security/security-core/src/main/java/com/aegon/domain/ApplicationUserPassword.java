package com.aegon.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lang.EmailException;
import lang.SimpleId;

public class ApplicationUserPassword extends SimpleId<String> {

	private ApplicationUserPassword(String internal) {
		super(internal);
	}

	public static ApplicationUserPassword valueOf(String password){
		// TODO
		return new ApplicationUserPassword(password);
	}

	private static String validatePassword(String email) {
		final Matcher matcher = passwordPattern().matcher(email);
		if (!matcher.matches()) {
			throw ApplicationUserPasswordException.notValid();
		}
		return email;
	}

	private static Pattern passwordPattern() {
		final String regex = "[a-zA-Z0-9]+";
		return Pattern.compile(regex);
	}
}
