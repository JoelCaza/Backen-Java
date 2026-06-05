package com.webdev.clase2eventos.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretString;
    @Value("${jwt.expiration}")
    private long expirationTime;

    private Key secretKey;

    @PostConstruct
    protected void init(){
        this.secretKey = Keys.hmacShaKeyFor(secretString.getBytes());
    }

    public String generarToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ expirationTime))
                .signWith(secretKey,SignatureAlgorithm.HS256)
                .compact();
    }
    public String extraerEmail(String token){
        return obtenerClaims(token).getSubject();

    }

    private Claims obtenerClaims(String token){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

    }
    public boolean validarToken(String token){
        try{
            obtenerClaims(token);
            return true;

        }catch(Exception e){
            return false;
        }
    }
}
