package KidAttend.demo.dto.request.classroom;

import KidAttend.demo.entity.ClassStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class ClassSearchRequest {

    @Size(max = 100, message = "Tên không được vượt quá 100 ký tự")
    private String name;

    @Min(value = 1, message = "Tuổi phải lớn hơn hoặc bằng 1")
    @Max(value = 100, message = "Tuổi phải nhỏ hơn hoặc bằng 100")
    private Integer age;

    private ClassStatus status;

    @Min(value = 1, message = "Id phải lớn hơn hoặc bằng 1")
    private Long teacherId;
}