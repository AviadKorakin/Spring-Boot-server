package restR.logic.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class RestRBadRequestException  extends RuntimeException { 
	private static final long serialVersionUID = -352007701791712334L;

	public RestRBadRequestException() {
		super();
	}

	public RestRBadRequestException(String message) {
		super(message);
	}

	public RestRBadRequestException(Throwable cause) {
		super(cause);
	}

	public RestRBadRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}
