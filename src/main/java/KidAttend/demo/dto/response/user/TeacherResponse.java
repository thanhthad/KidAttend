package KidAttend.demo.dto.response.user;

import lombok.*;

@Getter
@Setter
@Builder
public class TeacherResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
}