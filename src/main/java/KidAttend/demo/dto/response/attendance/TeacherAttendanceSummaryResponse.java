package KidAttend.demo.dto.response.attendance;

public record TeacherAttendanceSummaryResponse(
        String className,
        Long totalAttendance,
        Long present,
        Long absent
) {
}