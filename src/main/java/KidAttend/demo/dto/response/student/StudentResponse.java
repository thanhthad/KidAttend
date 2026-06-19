package KidAttend.demo.dto.response.student;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class StudentResponse {

    private Long id;

    private Long classId;
    private String className;

    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;

    private String parentName;
    private String parentPhone;
    private String parentEmail;

    private String address;
    private String status;
}