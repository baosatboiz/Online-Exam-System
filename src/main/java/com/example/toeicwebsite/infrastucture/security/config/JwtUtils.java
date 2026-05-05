package com.example.toeicwebsite.infrastucture.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String secretKey;

    @Value("${app.jwt.expiration-seconds:3600}")
    private long expirationSeconds;

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(String email, List<String> role){
        return Jwts.builder()
                .setSubject(email)
                .claim("role",role)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(expirationSeconds)))
                .signWith(getSecretKey())
                .compact();
    }

    public String generateToken(String email, List<String> role, String picture){
        return Jwts.builder()
                .setSubject(email)
                .claim("role",role)
                .claim("picture",picture)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(expirationSeconds)))
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
