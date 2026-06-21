package KidAttend.demo.exception.attendance;

public class AttendanceAlreadyExistsException extends RuntimeException {
    public AttendanceAlreadyExistsException(String message) {
        super(message);
    }
}
