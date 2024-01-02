package org.choongang.configs;

import jakarta.servlet.http.HttpServletResponse;
import org.choongang.member.service.LoginFailureHandler;
import org.choongang.member.service.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity // 메서드 통제 등
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        /* 인증 설정 S(tart) - 로그인 */
        http.formLogin(f -> {
                         /// 변경될 수 있는 부분은 직접 설정
            f.loginPage("/member/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successHandler(new LoginSuccessHandler()) // 성공 시
                    .failureHandler(new LoginFailureHandler()); // 실패 시
        });

        // 로그아웃
        http.logout(c -> {
           c.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")) // 로그아웃 주소
                   .logoutSuccessUrl("/member/login"); // 로그아웃 후 이동 주소
        });
        /* 인증 설정 E(nd) - 로그인, 로그아웃 */


        /* 인가 설정 S - 접근 통제 */
        // 1) hasAuthority(..) hasAnyAuthority(...), hasRole, hasAnyRole
        // 2) ROLE_롤명칭
        http.authorizeHttpRequests(c -> {
            // 일부 페이지 허용 x
            c.requestMatchers("/mypage/**").authenticated() // 회원 전용
                    .requestMatchers("/admin/**")
                    .hasAnyAuthority("ADMIN", "MANAGER")
                    .anyRequest().permitAll(); // 그외 모든 페이지는 모두 접근 가능

        });

        // 페이지에 따라 익셉션 처리하기
        http.exceptionHandling(c -> {
            // 현재 허용되지 않는 페이지 이동 시(인가 설정x) login 페이지로 이동된다.
            // 익셉션 처리, 원하는 페이지로 이동
            // 양이 많을 때 AuthenticationEntryPoint 인터페이스 이용할 수 있음.
            c.authenticationEntryPoint((req, res, e) -> {
                String URL = req.getRequestURI();
                if(URL.indexOf("/admin") != -1) { // 관리자 페이지일 때
                    // 응답코드 401(권한 없음)
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                } else { // 회원전용 페이지
                    res.sendRedirect(req.getContextPath() + "/member/login");

                }

            });
        });
        /* 인가 설정 E - 접근 통제 */

        return http.build();
    }


    // 비밀번호 해시화 - BCrypt
    @Bean
     public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
     }
}
