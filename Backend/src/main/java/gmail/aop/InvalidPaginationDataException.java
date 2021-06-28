package gmail.aop;

public class InvalidPaginationDataException extends RuntimeException {

    private static final long serialVersionUID = -8864742884664458267L;

    public InvalidPaginationDataException() {
    }

    public InvalidPaginationDataException(String message) {
        super(message);
    }

    public InvalidPaginationDataException(Throwable cause) {
        super(cause);
    }

    public InvalidPaginationDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
