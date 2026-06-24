package KidAttend.demo.dto.response.attendancesetting;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class AttendanceSettingResponse {

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer allowLateMinutes;
}