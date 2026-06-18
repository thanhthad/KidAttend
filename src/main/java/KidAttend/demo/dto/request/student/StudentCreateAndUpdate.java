package KidAttend.demo.dto.request.student;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentCreateAndUpdate {

    private String fullName;

    private String gender;

    private LocalDate dateOfBirth;

    private String parentName;

    private String parentPhone;

    private String parentEmail;

    private String address;
}
