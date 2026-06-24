package KidAttend.demo.dto.response.attendance;

public record AttendanceFilterResponse(
        String className,
        String studentName,
        String status,
        String note
) {
}