package com.example.setty.common.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration  // Spring IoC 컨테이너에게 해당 클래스가 Bean 구성 클래스임을 알려주는 annotation
@EnableWebSecurity  // Spring Security 활성화하여 보안 설정 적용
public class SecurityConfig extends WebSecurityConfiguration {
    // WebSecurityConfiguration: Spring Security에서 HTTP 요청에 대한 기본 보안 구성 설정하는 클래스

    // @Bean: 메서드의 반환값을 IoC 컨테이너에 Bean으로 등록하기 위해 사용하는 annotation
    // @Bean이 붙은 메서드는 메서드의 리턴값을 IoC 컨테이너가 관리하는 객체인 Bean으로 등록하게 됨
    // 이렇게 등록된 Bean은 어플리케이션 전체에서 의존성 주입을 받을 수 있음
    // Spring Security의 핵심 요소인 Security Filter Chain
    // 요청이 들어올 때마다 실행되며, 설정에 따라 인증, 인가 등의 보안 작업 수행
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {  // HttpSecurity: HTTP 요청에 대한 보안 구성을 정의하는 객체
        http
                .authorizeHttpRequests(authorize -> authorize
                        // 특정 경로 패턴에 대한 요청 필터링
                        // 해당 URL 경로에 모든 사용자 접근 허용
                        .requestMatchers("/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().permitAll()  // 나머지 모든 요청도 접근 허용
                )
                // HTTP 응답 헤더의 보안 설정을 정의하는 메서드
                // HTTP 응답 헤더: 클라이언트에 전달되어 브라우저가 리소스를 처리하는 방식 제어
                .headers(headers -> headers
                        // .frameOptions: X-Fram_Options 헤더를 설정하여 클랙재킹 공격 방지
                        // frameOptions.sameOrigin(): 동일 출처에서만 iframe으로 페이지 로드 가능
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )
                // .csrf: CSRF 공격을 방지하는 메커니즘 (CSRF: 악성 사이트가 사용자로 하여금 자신이 인증된 웹 어플리케이션에서 원하지 않는 요청을 수행하도록 유도)
                .csrf(csrf -> csrf.disable())  // CSRF 보호 기능을 비활성화 (REST API에서는 일반적으로 CSRF 보호 필요 X)
                // .httpBasic: HTTP Basic 인증 방식을 사용하도록 하는 메서드
                // HTTP Basic 인증: 요청 헤더에 사용자 이름과 비밀번호를 인코딩하여 포함시키는 인증 방식
                .httpBasic(httpBasic -> httpBasic.disable())  // HTTP Basic 인증을 비활성화하여 보안 강화
                // sessionManagement: 세션 관리 방식을 정의하는 메서드
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )  // 어플리케이션이 stateless 방식으로 동작하게 만듦. 서버는 세션을 유지하지 않고 각 요청을 독립적으로 처리함
                // .cors: CORS 설정을 정의함
                // CORS: 웹 어플리케이션이 다른 출처에서 리소스를 요청할 수 있도록 허용하는 메커니즘
                .cors(cors -> cors.disable())
                // .formLogin: Spring Security의 기본 폼 기반 로그인 기능 활성화 및 비활성화하는 메서드
                .formLogin(formLogin -> formLogin.disable());  // 비활성화하여 API 서버가 로그인 페이지를 렌더링하지 않도록 설정

        return http.build();
    }

    // BCryptPasswordEncoder: 비밀번호를 암호화하는데 사용되는 PasswordEncoder의 구현체
    // BCrypt 알고리즘을 사용하여 비밀번호를 해싱함
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
