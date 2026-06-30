package KidAttend.demo.dto.request.attendance;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateAttendanceRequest {

    @NotNull(message = "studentId không được để trống")
    @Positive(message = "studentId phải là số dương")
    private Long studentId;

    @NotNull(message = "Ngày điểm danh không được để trống")
    @PastOrPresent(message = "Ngày điểm danh không được lớn hơn ngày hiện tại")
    private LocalDate attendanceDate;

    @Size(max = 255, message = "Ghi chú không được vượt quá 255 ký tự")
    private String note;
}