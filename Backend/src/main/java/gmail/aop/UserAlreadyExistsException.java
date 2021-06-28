package gmail.aop;

public class UserAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = -8864742884664458267L;

    public UserAlreadyExistsException() {
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
