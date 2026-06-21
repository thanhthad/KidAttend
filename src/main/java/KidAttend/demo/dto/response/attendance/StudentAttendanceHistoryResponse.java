package KidAttend.demo.dto.response.attendance;

import java.time.LocalDate;

public record StudentAttendanceHistoryResponse(
        LocalDate attendanceDate,
        String status,
        String note
) {
}