package com.aegon.domain;

import lang.SimpleId;

public class JwtToken extends SimpleId<String> {

	private JwtToken(String internal) {
		super(internal);
	}

	public static JwtToken valueOf(String internal) {
		return new JwtToken(internal);
	}

}
