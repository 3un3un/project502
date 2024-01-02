package org.choongang.member.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.choongang.commons.Utils;
import org.choongang.member.MemberUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;

// 실패 시
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        HttpSession session = request.getSession();

        // 로그인 실패 후 남은 session 정리
        MemberUtil.clearLoginData(session);

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        session.setAttribute("username", username);

        // 아이디 없을 때
        if(!StringUtils.hasText(username)) {
            session.setAttribute("NotBlank_userId", Utils.getMessage("NotBlank.userId"));
        }

        // 비밀번호 없을 때
        if(!StringUtils.hasText(password)) {
            session.setAttribute("NotBlank_password", Utils.getMessage("NotBlank.password"));

        }

        // 아이디, 비번이 있지만 실패한 경우 : 아이디로 조회되는 회원이 없거나 비번이 일치 x
        if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
            session.setAttribute("Global_error", Utils.getMessage("Fail.login", "errors"));

        }

        // 로그인 페이지로 이동
        response.sendRedirect(request.getContextPath()+"/member/login");

    }
}
