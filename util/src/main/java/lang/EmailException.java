package lang;

public class EmailException extends BaseException{

	private static final String EMAIL_NOT_VALID_MSG = "Provided email does not match email pattern";

	private EmailException(String msg) {
		super(msg);
	}

	public static EmailException notValid() {
		return new EmailException(EMAIL_NOT_VALID_MSG);
	}
}
