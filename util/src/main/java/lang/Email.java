package lang;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email extends SimpleId<String> {

	private Email(String internal) {
		super(internal);
	}

	public static Email valueOf(String email) {
		return new Email(validateEmail(email));
	}

	private static String validateEmail(String email) {
		final Matcher matcher = emailPattern().matcher(email);
		if (!matcher.matches()) {
			throw EmailException.notValid();
		}
		return email;
	}

	private static Pattern emailPattern() {
		final String regex = "^(.+)@(.+)$";
		return Pattern.compile(regex);
	}

}
