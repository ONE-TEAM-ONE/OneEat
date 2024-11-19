package com.sparta.oneeat.auth.filter;

import com.sparta.oneeat.auth.service.UserDetailsServiceImpl;
import com.sparta.oneeat.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "Security 인가 절차")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();

        if (url.startsWith("/api/auth/") || url.startsWith("/v3/api-docs") || url.startsWith("/swagger-ui") || url.equals("/swagger-ui.html")) {
            log.info("Auth 또는 Swagger Resource 접근했습니다, 접근 URL: " + url);
            filterChain.doFilter(request, response);
            return;
        }

        String tokenValue = jwtUtil.getTokenFromRequest(request);
        log.info("Header에서 받아온 JWT Token: " + tokenValue);

        if (!jwtUtil.validateToken(tokenValue)) {
            log.warn("비정상적인 JWT 토큰입니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
        log.info("JWT Token이 Parsing 되었습니다.");

        try {
            setAuthentication(info.getSubject());
        } catch (Exception e) {
            log.warn("SecurityContextHolder에 Context를 설정하지 못했습니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);

        context.setAuthentication(authentication);
        log.info("SecurityContext에 회원 정보가 설정되었습니다.");

        SecurityContextHolder.setContext(context);
        log.info("SecurityContextHolder에 SecurityContext가 설정되었습니다.");
    }

    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        log.info("회원 정보를 DB에서 불러옵니다.");

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}