package KidAttend.demo.dto.request.student;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateStudentRequest {

    private Long classId;

    private String fullName;

    private String gender;

    private LocalDate dateOfBirth;

    private String parentName;

    private String parentPhone;

    private String parentEmail;

    private String address;

    private String status;
}