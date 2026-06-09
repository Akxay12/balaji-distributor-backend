package com.balaji.distributor.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;


    public String generateToken(
            Long userId,
            String mobileno,
            String role
    ){

        return Jwts.builder()

                .claim(
                        "userId",
                        userId
                )

                .claim(
                        "role",
                        role
                )

                .setSubject(
                        mobileno
                )

                .setIssuedAt(
                        new Date()
                )

                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + jwtExpiration
                        )
                )

                .signWith(
                        Keys.hmacShaKeyFor(
                                secretKey.getBytes(StandardCharsets.UTF_8)
                        ),
                        SignatureAlgorithm.HS256
                )

                .compact();
    }


    public Claims extractClaims(
            String token
    ) {

        return Jwts.parser()

                .setSigningKey(
                        Keys.hmacShaKeyFor(
                                secretKey.getBytes(StandardCharsets.UTF_8)
                        )
                )

                .parseClaimsJws(
                        token
                )

                .getBody();
    }


    public Long extractUserId(
            String token
    ) {

        return extractClaims(token)
                .get(
                        "userId",
                        Long.class
                );
    }


    public String extractRole(
            String token
    ) {

        return extractClaims(token)
                .get(
                        "role",
                        String.class
                );
    }


    public boolean isTokenValid(
            String token
    ) {

        try {

            Claims claims =
                    extractClaims(token);

            return claims
                    .getExpiration()
                    .after(
                            new Date()
                    );

        }
        catch (Exception e) {

            return false;
        }
    }
}
