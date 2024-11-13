package com.sparta.oneeat.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.oneeat.auth.dto.LoginRequestDto;
import com.sparta.oneeat.auth.dto.LoginResponseDto;
import com.sparta.oneeat.auth.service.UserDetailsImpl;
import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.common.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // 로그인 요청 데이터 읽기
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            // 인증 요청 생성
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            throw new CustomException(ExceptionType.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();

        // JWT 생성 후 헤더에 추가
        String token = jwtUtil.createToken(
                userDetails.getUsername(),
                userDetails.getUser().getRole()
        );
        jwtUtil.addJwtToHeader(token, response);
        System.out.println("Response committed: " + response.isCommitted());

        LoginResponseDto loginResponse = new LoginResponseDto(
                userDetails.getUser().getId(),
                userDetails.getUsername()
        );

        // JSON 응답 설정
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), loginResponse);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

}