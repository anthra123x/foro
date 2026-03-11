package com.foro.foro.seguridad;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationMillis;

    public String generarToken(UserDetails userDetails) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + expirationMillis);
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(ahora)
                .setExpiration(expiracion)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean esValido(String token, UserDetails userDetails) {
        String username = obtenerUsuarioDelToken(token);
        return username.equals(userDetails.getUsername()) && !estaExpirado(token);
    }

    public String obtenerUsuarioDelToken(String token) {
        return getClaims(token).getSubject();
    }

    public boolean estaExpirado(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}