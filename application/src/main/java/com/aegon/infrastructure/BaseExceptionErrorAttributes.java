package com.aegon.infrastructure;

import java.util.Map;
import lang.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
@Slf4j
public class BaseExceptionErrorAttributes extends DefaultErrorAttributes {

	private static final String ERROR_MSG = "Error has been raised!";

	private static final String EXCEPTION = "Exception";

	private static final String DEF_EXCEPTION_MSG = "System exception";

	private static final String MESSAGE = "Message";

	private static final String DEF_MESSAGE_MSG = "Please check logs for more information";

	private static final String STATUS = "Status";

	@Override
	public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
		final Throwable error = getError(request);
		log.error(ERROR_MSG, error);
		final Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);
		fillAttributes(error, errorAttributes);
		return errorAttributes;
	}

	private void fillAttributes(Throwable err, Map<String, Object> errorAttributes) {
		if (err instanceof BaseException ex) {
			errorAttributes.put(EXCEPTION, ex.getClass().getSimpleName());
			errorAttributes.put(MESSAGE, ex.getMessage());
			errorAttributes.put(STATUS, HttpStatus.BAD_REQUEST);
		} else {
			errorAttributes.put(EXCEPTION, DEF_EXCEPTION_MSG);
			errorAttributes.put(MESSAGE, DEF_MESSAGE_MSG);
			errorAttributes.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
