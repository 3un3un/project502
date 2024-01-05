package org.choongang.member;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.member.entities.Authorities;
import org.choongang.member.entities.Member;
import org.springframework.stereotype.Component;

// 회원 편의 기능
@Component
@RequiredArgsConstructor
public class MemberUtil {

    private final HttpSession session;

    // 관리자 여부 체크
    public boolean isAdmin(){
        // 로그인 O
        if(isLogin()) {
            return getMember().getAuthorities()
                    .stream().map(Authorities::getAuthority)
                    .anyMatch(a -> a == Authority.ADMIN || a == Authority.MANAGER); // 한 개라도 매치되면
        }

        return false;

    }

    public boolean isLogin() {
        return getMember() != null;
    }

    public Member getMember() {
        Member member = (Member) session.getAttribute("member");
        return member;

    }
    public static void clearLoginData(HttpSession session) {
        // 로그인 성공/실패 후 남은 session 정리
        session.removeAttribute("username");
        session.removeAttribute("NotBlank_username");
        session.removeAttribute("NotBlank_password");
        session.removeAttribute("Global_error");
    }







}
