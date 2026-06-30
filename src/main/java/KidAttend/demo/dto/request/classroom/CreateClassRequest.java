package KidAttend.demo.dto.request.classroom;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateClassRequest {

    @NotBlank(message = "Tên lớp học không được để trống")
    @Size(min = 2, max = 100, message = "Tên lớp học phải từ 2 đến 100 ký tự")
    private String name;

    @NotNull(message = "Tuổi không được để trống")
    @Min(value = 1, message = "Tuổi phải lớn hơn hoặc bằng 1")
    @Max(value = 6, message = "Tuổi phải nhỏ hơn hoặc bằng 6")
    private Integer age;

    @NotNull(message = "Sức chứa không được để trống")
    @Min(value = 1, message = "Sức chứa phải lớn hơn hoặc bằng 1")
    @Max(value = 50, message = "Sức chứa phải nhỏ hơn hoặc bằng 50")
    private Integer capacity;

    @Min(value = 1, message = "TeacherId phải lớn hơn hoặc bằng 1")
    private Long teacherId;

    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String description;
}