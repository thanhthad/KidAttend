package KidAttend.demo.dto.response.attendance;

import java.time.LocalDate;

public record ClassAttendanceHistoryResponse(
        String studentName,
        LocalDate attendanceDate,
        String status
) {
}