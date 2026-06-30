package KidAttend.demo.service.impl;

import KidAttend.demo.entity.RefreshToken;
import KidAttend.demo.entity.User;
import KidAttend.demo.exception.refreshtoken.*;
import KidAttend.demo.repository.RefreshTokenRepository;
import KidAttend.demo.security.jwt.JwtUtil;
import KidAttend.demo.service.RefreshTokenService;
import KidAttend.demo.service.UserServiceDomain;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserServiceDomain userServiceDomain;

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    // ================= CREATE =================
    @Override
    public RefreshToken create(Long userId) {

        User user = userServiceDomain.getByUserId(userId);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiredAt(LocalDateTime.now().plusNanos(refreshExpirationMs * 1_000_000))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    // ================= VERIFY =================
    @Override
    public RefreshToken verify(String token) {

        if (token == null || token.isBlank()) {
            throw new InvalidRefreshTokenException("Refresh token không được để trống");
        }

        RefreshToken refreshToken =
                refreshTokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new InvalidRefreshTokenException("Không tìm thấy refresh token")
                        );

        if (refreshToken.isRevoked()) {
            throw new RefreshTokenRevokedException("Refresh token đã bị thu hồi");
        }

        if (refreshToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RefreshTokenExpiredException("Refresh token đã hết hạn");
        }

        return refreshToken;
    }

    // ================= FIND VALID =================
    @Transactional
    @Override
    public RefreshToken findValidByUser(Long userId) {

        return refreshTokenRepository
                .findFirstByUserIdAndRevokedFalseAndExpiredAtAfterOrderByExpiredAtDesc(
                        userId,
                        LocalDateTime.now()
                )
                .map(token -> token)
                .orElseGet(() -> create(userId));
    }

    // ================= GENERATE ACCESS TOKEN =================
    @Override
    public String generateAccessToken(String refreshTokenValue) {

        RefreshToken token = verify(refreshTokenValue);

        User user = token.getUser();

        return jwtUtil.generateAccessToken(
                user.getId(),
                user.getFullName(),
                user.getRole()
        );
    }

    // ================= REVOKE =================
    @Override
    @Transactional
    public void revoke(String refreshTokenValue) {

        RefreshToken token = verify(refreshTokenValue);

        token.setRevoked(true);

        refreshTokenRepository.save(token);
    }
}