package KidAttend.demo.exception.classroom;

public class ClassRoomAlreadyExistsException extends RuntimeException {
    public ClassRoomAlreadyExistsException(String message) {
        super(message);
    }
}
