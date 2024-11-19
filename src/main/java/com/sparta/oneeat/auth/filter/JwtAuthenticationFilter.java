package com.sparta.oneeat.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.oneeat.auth.dto.LoginRequestDto;
import com.sparta.oneeat.auth.dto.LoginResponseDto;
import com.sparta.oneeat.auth.service.UserDetailsImpl;
import com.sparta.oneeat.common.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "Security 인증 절차")
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
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
            log.info("인증을 요청한 사용자 ID: " + requestDto.getUsername());

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        log.info("인증에 성공했습니다.");

        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();

        String token = jwtUtil.createToken(
                userDetails.getUsername(),
                userDetails.getUser().getRole()
        );
        log.info("생성된 JWT Token: " + token);

        jwtUtil.addJwtToHeader(token, response);
        log.info("JWT Token을 Header에 추가했습니다.");

        LoginResponseDto loginResponse = new LoginResponseDto(
                userDetails.getUser().getId(),
                userDetails.getUsername()
        );

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), loginResponse);
        log.info("응답 데이터를 Response에 설정했습니다.");

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.warn("인증에 실패했습니다.");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

}