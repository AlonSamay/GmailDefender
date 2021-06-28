package gmail.aop;

public class FilterNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -8864742884664458267L;

    public FilterNotFoundException() {
    }

    public FilterNotFoundException(String message) {
        super(message);
    }

    public FilterNotFoundException(Throwable cause) {
        super(cause);
    }

    public FilterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
