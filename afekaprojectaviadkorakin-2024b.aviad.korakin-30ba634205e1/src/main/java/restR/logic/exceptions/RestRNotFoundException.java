package restR.logic.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class RestRNotFoundException  extends RuntimeException { 
	private static final long serialVersionUID = -352007701791712334L;

	public RestRNotFoundException() {
		super();
	}

	public RestRNotFoundException(String message) {
		super(message);
	}

	public RestRNotFoundException(Throwable cause) {
		super(cause);
	}

	public RestRNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
