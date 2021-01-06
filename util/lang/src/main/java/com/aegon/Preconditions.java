package com.aegon;

import java.util.Collection;

public class Preconditions {

	private Preconditions() {
	}

	public static <T> T requireNonNull(T object) {
		if (object == null) {
			throw new NullPointerException();
		}
		return object;
	}

	public static String requireNonEmpty(String nullableParam) {
		final var param = requireNonNull(nullableParam);
		final var isNotEmpty = !param.trim().isEmpty();
		return checkArgument(param, isNotEmpty, "argument is empty");
	}

	public static String requireMaxLength(String string, int maxChars) {
		final var param = requireNonNull(string);
		final var isExceeding = string.length() > maxChars;
		return checkArgument(param, !isExceeding, "argument is exceeding maximum length");
	}

	public static <T extends Collection> T requireNonEmpty(T collection) {
		final var param = requireNonNull(collection);
		final var isNotEmpty = !param.isEmpty();
		return checkArgument(param, isNotEmpty, "argument is empty");
	}

	public static <T> T requireNonNull(T object, String message, Object... messageParams) {
		if (object == null) {
			throw new NullPointerException(String.format(message, messageParams));
		}
		return object;
	}

	public static <T> T requireNonNull(T object, String message) {
		if (object == null) {
			throw new NullPointerException(message);
		}
		return object;
	}

	public static <T> T checkArgument(T argument, boolean condition, String message) {
		if (!condition) {
			throw new IllegalArgumentException(message);
		}
		return argument;
	}

}
