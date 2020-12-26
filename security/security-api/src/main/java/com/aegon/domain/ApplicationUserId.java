package com.aegon.domain;

import lang.SimpleId;

public class ApplicationUserId extends SimpleId<String> {

	private ApplicationUserId(String internal) {
		super(internal);
	}

	public static ApplicationUserId valueOf(String id) {
		return new ApplicationUserId(id);
	}
}
