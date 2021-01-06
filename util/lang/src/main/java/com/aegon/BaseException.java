package com.aegon;

public class BaseException extends RuntimeException {

	protected BaseException(String msg) {
		super(msg);
	}

	public static BaseException with(String msg) {
		return new BaseException(msg);
	}

}
