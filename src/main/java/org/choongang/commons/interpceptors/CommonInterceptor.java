package org.choongang.commons.interpceptors;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class CommonInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        checkDevice(request);

        return true;
    }

    // PC, 모바일 수동 변경 처리
    // device 쿼리스트링으로 PC/mobile 구분한다
    // PC : PC 뷰, Mobile : mobile 뷰
    private void checkDevice(HttpServletRequest request) {
        String device = request.getParameter("device");
        if(!StringUtils.hasText(device)) {
            return; // 메서드 종료
        }

        // 대문자로 통일하여 체크
        device = device.toUpperCase().equals("MOBILE") ? "MOBILE" : "PC";

        // 세션에 저장
        HttpSession session = request.getSession();
        session.setAttribute("device", device);

    }
}
