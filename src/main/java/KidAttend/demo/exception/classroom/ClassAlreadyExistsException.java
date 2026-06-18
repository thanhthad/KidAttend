package KidAttend.demo.exception.classroom;

public class ClassAlreadyExistsException extends RuntimeException {
    public ClassAlreadyExistsException(String message) {
        super(message);
    }
}
