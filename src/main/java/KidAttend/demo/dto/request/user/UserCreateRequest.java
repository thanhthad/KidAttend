package KidAttend.demo.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {

    @NotBlank(message = "Full name must not be blank")
    @Size(
            min = 2,
            max = 100,
            message = "Full name must be between 2 and 100 characters"
    )
    private String fullName;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email address")
    @Size(
            max = 100,
            message = "Email must not exceed 100 characters"
    )
    private String email;

    @Pattern(
            regexp = "^(0[3|5|7|8|9])[0-9]{8}$",
            message = "Invalid phone number"
    )
    private String phone;

    @NotBlank(message = "Password must not be blank")
    @Size(
            min = 8,
            max = 50,
            message = "Password must be between 8 and 50 characters"
    )
    private String password;

}