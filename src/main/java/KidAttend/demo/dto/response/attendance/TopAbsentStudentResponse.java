package KidAttend.demo.dto.response.attendance;

public record TopAbsentStudentResponse(
        Long studentId,
        String studentName,
        Long absentDays
) {
}
