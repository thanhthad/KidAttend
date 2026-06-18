package KidAttend.demo.dto.request.student;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BulkCreateStudentRequest {

    @NotEmpty
    @Valid
    private List<StudentCreateAndUpdate> students;
}
