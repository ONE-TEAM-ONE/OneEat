package com.sparta.oneeat.auth.filter;

import com.sparta.oneeat.auth.service.UserDetailsServiceImpl;
import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String url = req.getRequestURI();

        if (url.startsWith("/api/auth/")) {
            filterChain.doFilter(req, res);
            return;
        }

        String tokenValue = jwtUtil.getTokenFromRequest(req);

        if (StringUtils.hasText(tokenValue)) {
            tokenValue = jwtUtil.substringToken(tokenValue);

            if (!jwtUtil.validateToken(tokenValue))
                throw new CustomException(ExceptionType.AUTH_UNAUTHORIZED_TOKEN);

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                throw new CustomException(ExceptionType.AUTH_UNAUTHORIZED_ERROR);
            }
        }

        filterChain.doFilter(req, res);
    }

    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}