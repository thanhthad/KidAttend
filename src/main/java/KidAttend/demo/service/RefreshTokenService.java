package KidAttend.demo.service;

import KidAttend.demo.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken create(Long userId);

    RefreshToken verify(String token);

    RefreshToken findValidByUser(Long userId );

    void revoke(String refreshToken);

    String generateAccessToken(String refreshToken);
}