package ch.sbb.roteroktober.server.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Löst einen HTTP 409, Conflict aus.
 */
@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateEntryException extends RuntimeException {

    public DuplicateEntryException() {
    }

    public DuplicateEntryException(String message) {
        super(message);
    }

    public DuplicateEntryException(String message, Throwable cause) {
        super(message, cause);
    }
}
