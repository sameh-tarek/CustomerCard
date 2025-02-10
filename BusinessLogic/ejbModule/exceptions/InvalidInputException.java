package exceptions;

public class InvalidInputException extends CustomerCardException {
    private static final long serialVersionUID = 1L;

    public InvalidInputException(String message) {
        super(message, 400);
    }
}