package KidAttend.demo.dto.request.attendance;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateAttendanceRequest {

    @NotNull(message = "studentId must not be null")
    @Positive(message = "studentId must be a positive number")
    private Long studentId;

    @NotNull(message = "attendanceDate must not be null")
    @PastOrPresent(message = "attendanceDate cannot be in the future")
    private LocalDate attendanceDate;

    @Size(max = 255, message = "note must not exceed 255 characters")
    private String note;
}