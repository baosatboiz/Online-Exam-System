package com.example.toeicwebsite.infrastucture.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {
    private final String secretKey ="day_la_bi_mat_cua_bao_it_nam_4_khong_duoc_ai_biet_dau_nhe";
    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
    public String generateToken(String email, List<String> role){
        return Jwts.builder()
                .setSubject(email)
                .claim("role",role)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(3600)))
                .signWith(getSecretKey())
                .compact();
    }
    public Claims parseToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token).getBody();
    }
}
