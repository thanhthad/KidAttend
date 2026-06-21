package KidAttend.demo.dto.response.attendance;

public record AttendanceStatusSummaryResponse(
        String status,
        Long total
) {
}