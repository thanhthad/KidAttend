package KidAttend.demo.dto.response.attendance;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AttendanceResponse {

    private Long id;

    private Long studentId;

    private String studentName;

    private LocalDate attendanceDate;

    private String status;

    private String note;

    private Long createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}