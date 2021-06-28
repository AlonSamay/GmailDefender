package gmail.aop;

public class FilterAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = -8864742884664458267L;

    public FilterAlreadyExistsException() {
    }

    public FilterAlreadyExistsException(String message) {
        super(message);
    }

    public FilterAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public FilterAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
