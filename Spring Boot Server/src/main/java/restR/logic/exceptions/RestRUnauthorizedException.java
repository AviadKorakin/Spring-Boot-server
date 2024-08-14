package restR.logic.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class RestRUnauthorizedException  extends RuntimeException { 
	private static final long serialVersionUID = -352007701791712334L;

	public RestRUnauthorizedException() {
		super();
	}

	public RestRUnauthorizedException(String message) {
		super(message);
	}

	public RestRUnauthorizedException(Throwable cause) {
		super(cause);
	}

	public RestRUnauthorizedException(String message, Throwable cause) {
		super(message, cause);
	}
}
