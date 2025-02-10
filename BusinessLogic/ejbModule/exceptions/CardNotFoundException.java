package exceptions;

import java.io.Serializable;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

@ApplicationException(rollback = true)
public class CardNotFoundException extends RuntimeException implements Serializable{
    private static final long serialVersionUID = 1L;
    private final int statusCode;

    public CardNotFoundException(String message) {
        super(message);
        this.statusCode = Response.Status.NOT_FOUND.getStatusCode();
    }

    public CardNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = Response.Status.NOT_FOUND.getStatusCode();
    }

    public CardNotFoundException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public CardNotFoundException(String message, Throwable cause, int statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
