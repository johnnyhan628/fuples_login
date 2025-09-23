package com.fuples.config.security;

import com.fuples.config.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableConfigurationProperties(SecurityProperties.class)
//@EnableMethodSecurity //@PreAuthorize("hasRole('ROLE_INACTIVE')") 사용시 필요
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final SecurityProperties securityProperties;

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //Spring security 에서 세션 미사용
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                                // permitAllAntPatterns에 대해서는 모두 접근 허용
                                .requestMatchers(securityProperties.getPermitAll()).permitAll()
                                // api/users 패턴은 토큰만 있으면 접근 가능
                                .requestMatchers(securityProperties.getAuthenticated()).authenticated()
                                .anyRequest().authenticated()
                )
                .exceptionHandling((exception) -> exception
                        //인증 오류 처리
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        //권한 오류 처리
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
        ;

        return http.build();
    }

    /**
     * cors 설정: 웹 애플리케이션이 다른 출처(도메일, 프로토콜, 포트)의 리소스에 접근할 수 있도록 허용하는 방법
     * api 대한 요청을 다른 도메인에서 받을 수 있음.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowCredentials(true); // client가 인증 정보를 포함하여 요청을 보낼 수 있도록 허용 (쿠키나 http 인증 헤더를 포함한 요청이 가능해짐)
        config.setMaxAge(3600L); //preflight에 대한 캐싱 시간 지정 - 3600초 (1시간) 동안 같은 요청을 다시 보내지 않아도 됨

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
