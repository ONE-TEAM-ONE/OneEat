package com.sparta.oneeat.common.util;

import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.user.entity.UserRoleEnum;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final String secretKey = Dotenv.load().get("JWT_SECRET_KEY");
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(String username, UserRoleEnum role) {
        Date now = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TOKEN_TIME)) // 1시간
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    // Header에서 토큰 추가하기
    public void addJwtToHeader(String token, HttpServletResponse response) {
        response.setHeader(AUTHORIZATION_HEADER, token);
    }

    // Header에서 토큰 가져오기
    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(BEARER_PREFIX.length());
        }
        throw new CustomException(ExceptionType.AUTH_NOT_FOUND_TOKEN);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            throw new CustomException(ExceptionType.AUTH_INVALID_SIGNATURE_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ExceptionType.AUTH_EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(ExceptionType.AUTH_UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ExceptionType.AUTH_EMPTY_CLAIMS_TOKEN);
        }
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

}