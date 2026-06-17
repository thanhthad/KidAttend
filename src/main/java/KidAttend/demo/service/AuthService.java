package KidAttend.demo.service;


import KidAttend.demo.dto.request.auth.LoginRequest;
import KidAttend.demo.dto.request.auth.RegisterRequest;
import KidAttend.demo.dto.response.auth.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse generateAccessToken(String refreshToken);

    void logout(String refreshToken);
}