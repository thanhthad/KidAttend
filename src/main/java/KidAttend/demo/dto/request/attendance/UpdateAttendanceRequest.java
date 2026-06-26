package KidAttend.demo.dto.request.attendance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAttendanceRequest {

    @NotNull
    private Long attendanceId;

    @NotBlank(message = "status must not be blank")
    @Pattern(
            regexp = "PRESENT|ABSENT",
            message = "status must be PRESENT or ABSENT"
    )
    private String status;

    @Size(max = 255, message = "note must not exceed 255 characters")
    private String note;
}