package KidAttend.demo.exception.refreshtoken;

public class RefreshTokenRevokedException extends RuntimeException {
    public RefreshTokenRevokedException(String message) {
        super(message);
    }
}
