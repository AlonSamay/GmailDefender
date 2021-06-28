package gmail.aop;

public class IncompatibleFilterDetailsException extends RuntimeException {

    private static final long serialVersionUID = -8864742884664458267L;

    public IncompatibleFilterDetailsException() {
    }

    public IncompatibleFilterDetailsException(String message) {
        super(message);
    }

    public IncompatibleFilterDetailsException(Throwable cause) {
        super(cause);
    }

    public IncompatibleFilterDetailsException(String message, Throwable cause) {
        super(message, cause);
    }
}
