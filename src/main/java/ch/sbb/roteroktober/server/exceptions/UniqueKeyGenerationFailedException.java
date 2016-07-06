package ch.sbb.roteroktober.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Diese Exception wird ausgelöst, wenn kein eindeutiger Schlüssel erstellt werden konnte
 */
@ResponseStatus(code = HttpStatus.CONFLICT)
public class UniqueKeyGenerationFailedException extends RuntimeException {
    public UniqueKeyGenerationFailedException() {
    }

    public UniqueKeyGenerationFailedException(String message) {
        super(message);
    }

    public UniqueKeyGenerationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
