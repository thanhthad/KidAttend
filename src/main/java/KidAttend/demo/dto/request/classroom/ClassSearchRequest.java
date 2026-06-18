package KidAttend.demo.dto.request.classroom;

import KidAttend.demo.entity.ClassStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class ClassSearchRequest {

    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Min(value = 1, message = "Age must be >= 1")
    @Max(value = 100, message = "Age must be <= 100")
    private Integer age;

    private ClassStatus status;

    private Long teacherId;
}