package KidAttend.demo.controller;

import KidAttend.demo.common.response.ResponseData;
import KidAttend.demo.dto.request.auth.LoginRequest;
import KidAttend.demo.dto.request.auth.RefreshTokenRequest;
import KidAttend.demo.dto.request.auth.RegisterRequest;
import KidAttend.demo.dto.response.auth.AuthResponse;
import KidAttend.demo.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Authentication", description = "Auth APIs")
public class AuthController {

    private final AuthService authService;

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request
    ) {

        AuthResponse response = authService.login(request);

        return ResponseData.success(
                response,
                "Đăng nhập thành công",
                HttpStatus.OK
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        AuthResponse response = authService.register(request);

        return ResponseData.success(
                response,
                "Đăng ký thành công",
                HttpStatus.OK
        );
    }

    // ================= REFRESH TOKEN =================
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {

        AuthResponse authResponse = authService.generateAccessToken(request.getRefreshToken());

        return ResponseData.success(
                authResponse,
                "Tạo access token mới thành công",
                HttpStatus.OK
        );
    }

    // ================= LOGOUT =================
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @Valid @RequestBody RefreshTokenRequest request
    ) {

        authService.logout(request.getRefreshToken());

        return ResponseData.success(
                null,
                "Đăng xuất thành công",
                HttpStatus.OK
        );
    }
}