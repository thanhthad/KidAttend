package KidAttend.demo.dto.response.attendance;

public record AttendanceDetailResponse(
        Long studentId,
        String studentName,
        String className,
        String status
) {
}
