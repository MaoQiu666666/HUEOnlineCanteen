package com.hue.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "66666666ItsMySecretKey19186836970";
    private static final long EXPIRATION_TIME = 1000*60*60*24;
    private static Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**生成 Token
     * claims 自定义数据：userId,userName,role等
     * 返回 jwt token字符串
     */
    public String generateToken(Long userId, String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId",userId);
        claims.put("username",username);
        claims.put("role",role);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 Token，获取里面所有的自定义数据
     */
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从 token中获取用户名和角色
     * get("key",类型.class)自动将载荷中的值转为指定类型。
     */
    public Long getUserIdFromToken(String token) {
        return parseToken(token).get("userId",Long.class);
    }public String getUserNameFromToken(String token) {
        return parseToken(token).get("username",String.class);
    }
    public String getRoleFromToken(String token) {
        return parseToken(token).get("role",String.class);
    }

    /**
     * 验证token是否合法（未篡改，未过期）
     * 抛出JJWT自己封装的异常
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            System.out.println("Token无效！");
        }
        return false;
    }
}
