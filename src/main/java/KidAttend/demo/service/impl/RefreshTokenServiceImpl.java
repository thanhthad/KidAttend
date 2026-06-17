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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Log4j2
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserServiceDomain userServiceDomain;

    @Value("${jwt.refresh-expiration-days}")
    private long refreshExpirationDays;

    // ================= CREATE =================
    @Override
    public RefreshToken create(Long userId) {

        User user = userServiceDomain.getByUserId(userId);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiredAt(LocalDateTime.now().plusDays(refreshExpirationDays))
                .revoked(false)
                .build();

        RefreshToken saved = refreshTokenRepository.save(refreshToken);

        log.info("AUTH_EVENT | action=REFRESH_TOKEN_CREATED | userId={} | tokenId={}",
                user.getId(), saved.getId());

        return saved;
    }

    // ================= VERIFY =================
    @Override
    public RefreshToken verify(String token) {

        if (token == null || token.isBlank()) {
            throw new InvalidRefreshTokenException("Refresh token is missing");
        }

        RefreshToken refreshToken =
                refreshTokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new InvalidRefreshTokenException("Refresh token not found")
                        );

        if (refreshToken.isRevoked()) {
            throw new RefreshTokenRevokedException("Refresh token has been revoked");
        }

        if (refreshToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RefreshTokenExpiredException("Refresh token has expired");
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
                .map(token -> {
                    log.info("AUTH_EVENT | action=REFRESH_TOKEN_REUSE | userId={} | tokenId={}",
                            userId, token.getId());
                    return token;
                })
                .orElseGet(() -> create(userId));
    }

    // ================= GENERATE ACCESS TOKEN =================
    @Override
    public String generateAccessToken(String refreshTokenValue) {

        RefreshToken token = verify(refreshTokenValue);

        User user = token.getUser();

        String accessToken = jwtUtil.generateAccessToken(
                user.getId(),
                user.getFullName(),
                user.getRole()
        );

        log.info("AUTH_EVENT | action=ACCESS_TOKEN_REFRESH | userId={} | tokenId={}",
                user.getId(), token.getId());

        return accessToken;
    }

    // ================= REVOKE =================
    @Override
    @Transactional
    public void revoke(String refreshTokenValue) {

        RefreshToken token = verify(refreshTokenValue);

        token.setRevoked(true);

        refreshTokenRepository.save(token);

        log.info("AUTH_EVENT | action=REFRESH_TOKEN_REVOKED | userId={} | tokenId={}",
                token.getUser().getId(),
                token.getId());
    }
}