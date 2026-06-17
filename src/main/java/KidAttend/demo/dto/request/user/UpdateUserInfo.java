package KidAttend.demo.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInfo {

    @Size(min = 2, max = 100,
            message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @Pattern(
            regexp = "^(0[3|5|7|8|9])[0-9]{8}$",
            message = "Invalid phone number"
    )
    private String phone;

    @Email(message = "Invalid email address")
    @Size(max = 100,
            message = "Email must not exceed 100 characters")
    private String email;
}