package com.sparta.oneeat.common.config;

import com.sparta.oneeat.auth.service.UserDetailsServiceImpl;
import com.sparta.oneeat.auth.filter.JwtAuthenticationFilter;
import com.sparta.oneeat.auth.filter.JwtAuthorizationFilter;
import com.sparta.oneeat.common.util.JwtUtil;
import com.sparta.oneeat.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());

        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) -> {

            // Swagger 파일 허용
            authorizeHttpRequests.requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
            ).permitAll();

            // 회원가입, 로그인 허용
            authorizeHttpRequests.requestMatchers("/api/auth/**").permitAll();

            authorizeHttpRequests.requestMatchers("/error").permitAll();

            authorizeHttpRequests.requestMatchers(HttpMethod.DELETE, "/api/**")
                    .hasAnyAuthority(
                            UserRoleEnum.MANAGER.getAuthority(),
                            UserRoleEnum.MASTER.getAuthority()
                    );

            authorizeHttpRequests.requestMatchers("/api/user", "/api/user/**")
                    .hasAnyAuthority(
                            UserRoleEnum.CUSTOMER.getAuthority(),
                            UserRoleEnum.OWNER.getAuthority()
                    );

            authorizeHttpRequests.requestMatchers(HttpMethod.GET, "/api/store", "/api/store/**")
                    .hasAnyAuthority(
                            UserRoleEnum.CUSTOMER.getAuthority(),
                            UserRoleEnum.OWNER.getAuthority(),
                            UserRoleEnum.MANAGER.getAuthority(),
                            UserRoleEnum.MASTER.getAuthority()
                    );

            authorizeHttpRequests.requestMatchers("/api/store/**", "/api/ai/**")
                    .hasAnyAuthority(UserRoleEnum.OWNER.getAuthority());

            authorizeHttpRequests.requestMatchers("/api/category", "/api/category/**")
                    .hasAnyAuthority(
                            UserRoleEnum.MANAGER.getAuthority(),
                            UserRoleEnum.MASTER.getAuthority()
                    );

            authorizeHttpRequests.requestMatchers(HttpMethod.POST, "/api/order")
                    .hasAnyAuthority(UserRoleEnum.CUSTOMER.getAuthority());
            authorizeHttpRequests.requestMatchers("/api/order/*/payment/**")
                    .hasAnyAuthority(UserRoleEnum.CUSTOMER.getAuthority());
            authorizeHttpRequests.requestMatchers("/api/order", "/api/order/**")
                    .hasAnyAuthority(
                            UserRoleEnum.CUSTOMER.getAuthority(),
                            UserRoleEnum.OWNER.getAuthority()
                    );

            authorizeHttpRequests.requestMatchers("/api/order/*/review", "/api/order/*/review/**")
                    .hasAnyAuthority(UserRoleEnum.CUSTOMER.getAuthority());
            // 모든 요청은 인증 필요
            authorizeHttpRequests.anyRequest().authenticated();


        });

        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

       return http.build();
    }
}
