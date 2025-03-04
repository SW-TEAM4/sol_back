package org.team4.sol_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  //  CSRF 보호 비활성화 (POST 요청 허용)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())  //  모든 요청 인증 없이 허용
                .formLogin(form -> form.disable()) //  로그인 폼 비활성화
                .httpBasic(httpBasic -> httpBasic.disable()); //  HTTP Basic 인증 비활성화

        return http.build();
    }
}
