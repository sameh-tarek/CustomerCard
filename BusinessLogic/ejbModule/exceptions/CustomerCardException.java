package exceptions;

import java.io.Serializable;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

@ApplicationException(rollback = true)
public class CustomerCardException extends RuntimeException implements Serializable{
    private static final long serialVersionUID = 1L;
    private final int statusCode;

    public CustomerCardException(String message) {
        super(message);
        this.statusCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    }

    public CustomerCardException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    }

    public CustomerCardException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public CustomerCardException(String message, Throwable cause, int statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
