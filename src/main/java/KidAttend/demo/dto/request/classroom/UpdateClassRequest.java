package KidAttend.demo.dto.request.classroom;

import KidAttend.demo.entity.ClassStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClassRequest {
    private String name;

    private Integer age;

    private Integer capacity;

    private Long teacherId;

    private String description;

    private ClassStatus status;
}