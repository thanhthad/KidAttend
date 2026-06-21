package KidAttend.demo.repository.projection;

import java.time.LocalDate;

public interface StudentAttendanceHistoryProjection {
    LocalDate getAttendanceDate();

    String getStatus();

    String getNote();
}
