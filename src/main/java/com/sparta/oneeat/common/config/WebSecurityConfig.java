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
            authorizeHttpRequests.requestMatchers("/api/auth/**", "/error").permitAll();

            // DELETE 메서드는 MANAGER, MASTER 권한만 허용
            authorizeHttpRequests.requestMatchers(HttpMethod.DELETE, "/api/**")
                    .hasAnyAuthority(
                            UserRoleEnum.MANAGER.getAuthority(),
                            UserRoleEnum.MASTER.getAuthority()
                    );

            // 유저 관련 API는 CUSTOMER, OWNER 권한만 허용
            authorizeHttpRequests.requestMatchers("/api/user", "/api/user/**")
                    .hasAnyAuthority(
                            UserRoleEnum.CUSTOMER.getAuthority(),
                            UserRoleEnum.OWNER.getAuthority()
                    );

            // 메뉴 조회는 전부 허용
            authorizeHttpRequests.requestMatchers(HttpMethod.GET, "/api/store/*/menus", "/api/store/*/menu/*")
                    .hasAnyAuthority(
                            UserRoleEnum.CUSTOMER.getAuthority(),
                            UserRoleEnum.OWNER.getAuthority(),
                            UserRoleEnum.MANAGER.getAuthority(),
                            UserRoleEnum.MASTER.getAuthority()
                    );

            // 메뉴 관련 데이터 변경은 OWNER 권한만 허용
            authorizeHttpRequests.requestMatchers("/api/store/*/menu/*", "/api/ai")
                    .hasAnyAuthority(UserRoleEnum.OWNER.getAuthority());

            // 가게 관련 조회는 모든 권한 허용
            authorizeHttpRequests.requestMatchers(HttpMethod.GET, "/api/store", "/api/store/**")
                    .hasAnyAuthority(
                            UserRoleEnum.CUSTOMER.getAuthority(),
                            UserRoleEnum.OWNER.getAuthority(),
                            UserRoleEnum.MANAGER.getAuthority(),
                            UserRoleEnum.MASTER.getAuthority()
                    );

            // 가게 생성은 OWNER만 허용
            authorizeHttpRequests.requestMatchers(HttpMethod.POST, "/api/store")
                    .hasAnyAuthority(
                            UserRoleEnum.OWNER.getAuthority()
                    );

            // 카테고리 관련 API는 MANAGER, MASTER 권한만 허용
            authorizeHttpRequests.requestMatchers("/api/category", "/api/category/**")
                    .hasAnyAuthority(
                            UserRoleEnum.MANAGER.getAuthority(),
                            UserRoleEnum.MASTER.getAuthority()
                    );

            // 주문과 결제는 CUSTOMER 권한만 허용
            authorizeHttpRequests.requestMatchers(HttpMethod.POST, "/api/order", "/api/order/*/payment")
                    .hasAnyAuthority(UserRoleEnum.CUSTOMER.getAuthority());

            // 주문과 결제 관련 데이터 변경은 CUSTOMER, OWNER 권한만 허용
            authorizeHttpRequests.requestMatchers("/api/order", "/api/order/**")
                    .hasAnyAuthority(
                            UserRoleEnum.CUSTOMER.getAuthority(),
                            UserRoleEnum.OWNER.getAuthority()
                    );

            // 결제 상태 변경은 OWNER, MANAGER, MASTER 권한만 허용
            authorizeHttpRequests.requestMatchers("/api/order", "/api/order/**")
                    .hasAnyAuthority(
                            UserRoleEnum.OWNER.getAuthority(),
                            UserRoleEnum.MANAGER.getAuthority(),
                            UserRoleEnum.MASTER.getAuthority()
                    );

            // 리뷰 조회는 모든 권한이 허용
            authorizeHttpRequests.requestMatchers(HttpMethod.GET, "/api/store/*/reviews")
                    .hasAnyAuthority(
                            UserRoleEnum.CUSTOMER.getAuthority(),
                            UserRoleEnum.OWNER.getAuthority(),
                            UserRoleEnum.MANAGER.getAuthority(),
                            UserRoleEnum.MASTER.getAuthority()
                    );

            // 리뷰 관련 데이터 변경은 CUSTOMER 권한만 허용
            authorizeHttpRequests.requestMatchers("/api/order/*/review", "/api/order/*/review/**")
                    .hasAnyAuthority(UserRoleEnum.CUSTOMER.getAuthority());

            // 이외의 모든 요청은 인증 필요
            authorizeHttpRequests.anyRequest().authenticated();

        });

        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
