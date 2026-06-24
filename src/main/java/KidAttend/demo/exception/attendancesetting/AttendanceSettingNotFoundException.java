package KidAttend.demo.exception.attendancesetting;

public class AttendanceSettingNotFoundException extends RuntimeException {
    public AttendanceSettingNotFoundException(String message) {
        super(message);
    }
}
