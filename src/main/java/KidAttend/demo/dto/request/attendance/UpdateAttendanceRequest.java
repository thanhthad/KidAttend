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

    @NotNull(message = "attendanceId không được để trống")
    private Long attendanceId;

    @NotBlank(message = "Trạng thái không được để trống")
    @Pattern(
            regexp = "PRESENT|ABSENT",
            message = "Trạng thái chỉ được là PRESENT hoặc ABSENT"
    )
    private String status;

    @Size(max = 255, message = "Ghi chú không được vượt quá 255 ký tự")
    private String note;
}