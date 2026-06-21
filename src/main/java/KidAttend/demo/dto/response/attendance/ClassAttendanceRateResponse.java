package KidAttend.demo.dto.response.attendance;

public record ClassAttendanceRateResponse(
        Long classId,
        String className,
        Long total,
        Long present,
        Double rate
) {
}