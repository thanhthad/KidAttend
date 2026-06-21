package KidAttend.demo.dto.response.attendance;

public record ClassAttendanceResponse(
        Long studentId,
        String studentName,
        String status,
        String note
) {
}