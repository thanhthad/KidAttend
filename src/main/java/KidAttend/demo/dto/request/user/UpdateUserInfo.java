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

    @Size(
            min = 2,
            max = 100,
            message = "Họ và tên phải từ 2 đến 100 ký tự"
    )
    private String fullName;

    @Pattern(
            regexp = "^(0[3|5|7|8|9])[0-9]{8}$",
            message = "Số điện thoại không hợp lệ"
    )
    private String phone;

    @Email(message = "Email không đúng định dạng")
    @Size(
            max = 100,
            message = "Email không được vượt quá 100 ký tự"
    )
    private String email;
}