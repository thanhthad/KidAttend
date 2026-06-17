package KidAttend.demo.security.jwt;

import KidAttend.demo.security.userdetails.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.access-expiration}")
    private long ACCESS_TOKEN_EXPIRE;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // ===== GENERATE TOKEN =====
    public String generateAccessToken(Long userId,
                                      String email,
                                      String role) {

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ===== VALIDATE =====
    public void validateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
    }

    // ===== CLAIMS =====
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ===== USER DETAILS =====
    public CustomUserDetails getUserDetails(String token) {

        Claims claims = extractClaims(token);

        Long userId = Long.parseLong(claims.getSubject());
        String fullName = claims.get("fullName", String.class);
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);

        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + role));

        return CustomUserDetails.builder()
                .id(userId)
                .fullName(fullName)
                .email(email)
                .role(role)
                .authorities(authorities)
                .build();
    }

    // ===== HELPERS =====
    public Long getUserId(String token) {
        return Long.parseLong(extractClaims(token).getSubject());
    }

    public String getRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    public String getFullName(String token) {
        return extractClaims(token).get("fullName", String.class);
    }
}