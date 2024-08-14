package restR.logic.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class RestRConflictException  extends RuntimeException { 
	private static final long serialVersionUID = -352007701791712334L;

	public RestRConflictException() {
		super();
	}

	public RestRConflictException(String message) {
		super(message);
	}

	public RestRConflictException(Throwable cause) {
		super(cause);
	}

	public RestRConflictException(String message, Throwable cause) {
		super(message, cause);
	}
}
