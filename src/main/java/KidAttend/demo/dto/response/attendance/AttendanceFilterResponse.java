package KidAttend.demo.dto.response.attendance;

public record AttendanceFilterResponse(
        String studentName,
        String status,
        String note
) {
}