package KidAttend.demo.dto.request.attendancesetting;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalTime;

@Data
public class AttendanceSettingUpdateRequest {

    private LocalTime startTime;

    private LocalTime endTime;

    @Min(value = 0, message = "allowLateMinutes must be >= 0")
    private Integer allowLateMinutes;
}