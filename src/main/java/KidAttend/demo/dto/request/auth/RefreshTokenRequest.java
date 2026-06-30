package KidAttend.demo.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RefreshTokenRequest {

    @NotBlank(message = "Refresh token không được để trống")
    @Size(min = 1, max = 1000, message = "Refresh token không được vượt quá 1000 ký tự")
    private String refreshToken;
}