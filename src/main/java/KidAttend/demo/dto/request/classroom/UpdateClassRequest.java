package KidAttend.demo.dto.request.classroom;

import KidAttend.demo.entity.ClassStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClassRequest {

    @NotBlank(message = "Class name is required")
    private String name;

    @NotNull(message = "Age is required")
    private Integer age;

    @NotNull(message = "Capacity is required")
    private Integer capacity;

    private Long teacherId;

    private String description;

    private ClassStatus status;
}