package com.example.park.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtils {
    
    private static final String SECRET_KEY="park-backend-jwt-secret-key-2025";

    private static final Key KEY=Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public static String getToken(Long userId,String username,Integer role){
        return Jwts.builder()
                   .setSubject(String.valueOf(userId))
                   .claim("username", username)
                   .claim("role", role)
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis()+86400000))//一日後token無効にする
                   .signWith(KEY,SignatureAlgorithm.HS256)
                   .compact();
    }

    public static Long parseToken(String token){
        Long userId=Long.valueOf(getClaims(token).getSubject());
        return userId;
    }

    public static String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // 去掉 "Bearer " 前缀
        }
    return null;
    }

    public static Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static Long getUserID(String token){
        Long userId=Long.valueOf(getClaims(token).getSubject());
        return userId;
    }

    public static String getUsername(String token){
        String username=getClaims(token).get("username",String.class);
        return username;
    }

    public static Integer getRole(String token){
        Integer role=getClaims(token).get("role",Integer.class);
        return role;
    }

    public static boolean isTokenExpired(String token){
        boolean isExpired=getClaims(token).getExpiration().before(new Date());
        return isExpired;
    }
}
