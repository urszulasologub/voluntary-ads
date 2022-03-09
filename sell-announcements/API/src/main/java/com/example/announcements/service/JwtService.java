package com.example.announcements.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.hmacSecret}")
    private String hmacSecret;

    public String  generateJwtToken(Integer userId, Date expiryDate) {
        return JWT.create()
                .withSubject(userId.toString())
                .withExpiresAt(expiryDate)
                .sign(Algorithm.HMAC256(hmacSecret));
    }

}
