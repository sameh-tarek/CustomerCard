package exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class CustomerCardException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerCardException(String message) {
        super(message);
    }

    public CustomerCardException(String message, Throwable cause) {
        super(message, cause);
    }
}
