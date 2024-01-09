package org.choongang.configs;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.member.service.LoginFailureHandler;
import org.choongang.member.service.LoginSuccessHandler;
import org.choongang.member.service.MemberInfoService;
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
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberInfoService memberInfoService;

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
                    .requestMatchers("/member/login", "/member/join").anonymous()
                    //.requestMatchers("/admin/**")
                    //.hasAnyAuthority("ADMIN", "MANAGER")
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
                } else if (URL.indexOf("/member/login") != -1 || URL.indexOf("/member/join") != -1) {
                    res.sendRedirect(req.getContextPath() + "/");
                } else { // 회원전용 페이지
                    res.sendRedirect(req.getContextPath() + "/member/login");

                }

            });
        });
        /* 인가 설정 E - 접근 통제 */

        /* 파일 업로드 이미지 삭제 후 */
        http.headers(c -> c.frameOptions(f -> f.sameOrigin()));

        /* 자동 로그인 설정 S */
        http.rememberMe(c -> {
            c.rememberMeParameter("autoLogin") // 자동 로그인으로 사용할 요청 파리미터 명, 기본값은 remember-me
                    .tokenValiditySeconds(60 * 60 * 24 * 30) // 로그인을 유지할 기간(30일로 설정), 기본값은 14일
                    .userDetailsService(memberInfoService) // 재로그인을 하기 위해서 인증을 위한 필요 UserDetailsService 구현 객체
                    .authenticationSuccessHandler(new LoginSuccessHandler()); // 자동 로그인 성공시 처리 Handler

        });
        /* 자동 로그인 설정 E */



        return http.build();
    }


    // 비밀번호 해시화 - BCrypt
    @Bean
     public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
     }
}
