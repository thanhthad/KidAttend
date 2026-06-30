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

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(
            min = 2,
            max = 100,
            message = "Họ và tên phải từ 2 đến 100 ký tự"
    )
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Size(
            max = 100,
            message = "Email không được vượt quá 100 ký tự"
    )
    private String email;

    @Pattern(
            regexp = "^(0[3|5|7|8|9])[0-9]{8}$",
            message = "Số điện thoại không hợp lệ"
    )
    private String phone;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(
            min = 8,
            max = 50,
            message = "Mật khẩu phải từ 8 đến 50 ký tự"
    )
    private String password;
}