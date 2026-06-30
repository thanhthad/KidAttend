package KidAttend.demo.dto.request.student;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BulkCreateStudentRequest {

    @NotNull(message = "ClassId không được để trống")
    private Long classId;

    @NotNull(message = "Danh sách học sinh không được để trống")
    private List<BulkStudentRequest> students;
}