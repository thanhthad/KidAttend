package KidAttend.demo.dto.request.classroom;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateClassRequest {

    @NotBlank(message = "Class name is required")
    @Size(min = 2, max = 100, message = "Class name must be 2-100 characters")
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be >= 1")
    @Max(value = 6, message = "Age must be <= 6")
    private Integer age;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be >= 1")
    @Max(value = 50, message = "Capacity must be <= 50")
    private Integer capacity;

    @Min(value = 1, message = "TeacherId must be >= 1")
    private Long teacherId;

    @Size(max = 500, message = "Description max 500 characters")
    private String description;

}