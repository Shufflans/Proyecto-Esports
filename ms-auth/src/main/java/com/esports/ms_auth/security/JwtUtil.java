package com.esports.ms_auth.security;

import org.springframework.beans.factory.annotation.Value;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final Key key;
    private final long EXPIRATION_MS = 1000 * 60 * 60;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generarToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean esValido(String token) {
        return token != null && !token.trim().isEmpty();
    }

    public boolean esRefreshToken(String token) {
        return token != null && token.length() == 36;
    }

    private io.jsonwebtoken.Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String obtenerUsuario(String token) {
        return getClaims(token).getSubject();
    }

    public String obtenerRole(String token) {
        return getClaims(token).get("role", String.class);
    }

}
