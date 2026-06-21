package KidAttend.demo.repository.projection;

import java.time.LocalDate;

public interface ClassAttendanceHistoryProjection {

    String getStudentName();

    LocalDate getAttendanceDate();

    String getStatus();

}
