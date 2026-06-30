package KidAttend.demo.dto.request.student;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateStudentRequest {

    @NotNull(message = "ClassId không được để trống")
    private Long classId;

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    @NotBlank(message = "Giới tính không được để trống")
    private String gender;

    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Tên phụ huynh không được để trống")
    private String parentName;

    @Pattern(regexp = "^0[0-9]{9}$", message = "Số điện thoại không đúng định dạng")
    private String parentPhone;

    @Email(message = "Email không đúng định dạng")
    private String parentEmail;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotBlank(message = "Trạng thái không được để trống")
    private String status;
}