package KidAttend.demo.dto.request.student;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BulkStudentRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Parent name is required")
    private String parentName;

    @NotBlank(message = "Parent phone is required")
    @Pattern(regexp = "^0[0-9]{9}$", message = "Invalid phone format")
    private String parentPhone;

    @NotBlank(message = "Parent email is required")
    @Email(message = "Invalid email format")
    private String parentEmail;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Status is required")
    private String status;
}