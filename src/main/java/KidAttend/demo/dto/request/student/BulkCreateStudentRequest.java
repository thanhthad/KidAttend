package KidAttend.demo.dto.request.student;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BulkCreateStudentRequest {

    @NotNull(message = "ClassId is required")
    private Long classId;

    @NotNull(message = "Student list is required")
    private List<BulkStudentRequest> students;
}