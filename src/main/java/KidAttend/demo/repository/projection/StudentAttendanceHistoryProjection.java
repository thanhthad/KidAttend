package KidAttend.demo.repository.projection;

import java.time.LocalDate;

public interface StudentAttendanceHistoryProjection {
    LocalDate attendanceDate();

    String status();

    String note();
}
