package KidAttend.demo.dto.response.attendance;

public record StudentAttendanceStatisticResponse(
        Long studentId,
        Long presentDays,
        Long absentDays
) {
}
