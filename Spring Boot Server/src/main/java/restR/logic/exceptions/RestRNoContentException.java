package restR.logic.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NO_CONTENT)
public class RestRNoContentException  extends RuntimeException { 
	private static final long serialVersionUID = -352007701791712334L;

	public RestRNoContentException() {
		super();
	}

	public RestRNoContentException(String message) {
		super(message);
	}

	public RestRNoContentException(Throwable cause) {
		super(cause);
	}

	public RestRNoContentException(String message, Throwable cause) {
		super(message, cause);
	}
}
