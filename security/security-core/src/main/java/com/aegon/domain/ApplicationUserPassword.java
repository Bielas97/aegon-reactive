package com.aegon.domain;

import com.aegon.SimpleId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicationUserPassword extends SimpleId<String> {

	private ApplicationUserPassword(String internal) {
		super(internal);
	}

	public static ApplicationUserPassword valueOf(String password) {
		// TODO
		//		return new ApplicationUserPassword(validatePassword(password));
		return new ApplicationUserPassword(password);
	}

	private static String validatePassword(String password) {
		final Matcher matcher = passwordPattern().matcher(password);
		if (!matcher.matches()) {
			throw ApplicationUserPasswordException.notValid();
		}
		return password;
	}

	private static Pattern passwordPattern() {
		final String regex = "[a-zA-Z0-9]+";
		return Pattern.compile(regex);
	}
}
