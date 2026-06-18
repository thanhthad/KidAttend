package KidAttend.demo.dto.response.student;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {

    private Long id;

    private String fullName;

    private String gender;

    private LocalDate dateOfBirth;

    private String parentName;

    private String parentPhone;

    private String parentEmail;

    private String address;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
