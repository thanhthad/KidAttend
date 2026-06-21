package KidAttend.demo.dto.request.attendance;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAttendanceRequest {

    private String status;

    private String note;
}