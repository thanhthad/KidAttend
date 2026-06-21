package KidAttend.demo.dto.request.attendance;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateAttendanceRequest {

    private Long studentId;

    private LocalDate attendanceDate;

    private String status;

    private String note;

    private Long createdBy;
}