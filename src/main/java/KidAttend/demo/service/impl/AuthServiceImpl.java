package KidAttend.demo.service.impl;

import KidAttend.demo.dto.request.auth.LoginRequest;
import KidAttend.demo.dto.request.auth.RegisterRequest;
import KidAttend.demo.dto.response.auth.AuthResponse;
import KidAttend.demo.entity.RefreshToken;
import KidAttend.demo.entity.User;
import KidAttend.demo.exception.user.UserAlreadyExistsException;
import KidAttend.demo.exception.user.UserNotFoundException;
import KidAttend.demo.repository.UserRepository;
import KidAttend.demo.security.jwt.JwtUtil;
import KidAttend.demo.service.AuthService;
import KidAttend.demo.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(
                    "User already exists with email: " + request.getEmail()
            );
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .status("ACTIVE")
                .build();

        userRepository.save(user);

        RefreshToken refreshTokenEntity =
                refreshTokenService.create(user.getId());

        String accessToken = jwtUtil.generateAccessToken(
                user.getId(),
                user.getFullName(),
                user.getRole()
        );

        return AuthResponse.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshTokenEntity.getToken())
                .build();
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with email: " + request.getEmail()
                        )
                );

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid password");
        }

        RefreshToken refreshTokenEntity =
                refreshTokenService.findValidByUser(user.getId());

        String accessToken = jwtUtil.generateAccessToken(
                user.getId(),
                user.getFullName(),
                user.getRole()
        );

        return AuthResponse.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshTokenEntity.getToken())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse generateAccessToken(String refreshToken) {

        String accessToken =
                refreshTokenService.generateAccessToken(refreshToken);

        RefreshToken tokenEntity =
                refreshTokenService.verify(refreshToken);

        User user = tokenEntity.getUser();

        return AuthResponse.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        refreshTokenService.revoke(refreshToken);
    }
}