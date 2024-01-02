package org.choongang.commons;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class Utils {

    private final HttpServletRequest request;

    private final HttpSession session;

    private static final ResourceBundle commonsBundle;
    private static final ResourceBundle validatiosnBundle;
    private static final ResourceBundle errorsBundle;

    static {
        commonsBundle = ResourceBundle.getBundle("messages.commons");
        validatiosnBundle = ResourceBundle.getBundle("messages.validations");
        errorsBundle = ResourceBundle.getBundle("messages.errors");
    }


    // 모바일인지 아닌지 체크하는 편의 메서드
    public boolean isMobile() {
        // 모바일 수동 전환 모드 체크
        String device = (String) session.getAttribute("device");
        if(StringUtils.hasText(device)) {
           return device.equals("MOBILE");
        }

        // 요청 헤더 : User-Agent 가져오기
        String ua = request.getHeader("User-Agent");

        String pattern = ".*(iPhone|iPod|iPad|BlackBerry|Android|Windows CE|LG|MOT|SAMSUNG|SonyEricsson).*";

        return ua.matches(pattern);

    }

    public String tpl(String path) {
        // 장비에 따라 mobile or front 템플릿
        String prefix = isMobile() ? "mobile/" : "front/";

        return prefix + path;
    }

    // 메세지 편하게 사용하기(resources > messages 파일들)
    public static String getMessage(String code, String type) {
        type = StringUtils.hasText(type) ? type : "validations"; // type = null -> "validations"

        ResourceBundle bundle = null;
        if(type.equals("commons")) {
            bundle = commonsBundle;
        } else if(type.equals("errors")) {
            bundle = errorsBundle;
        } else {
            bundle = validatiosnBundle;
        }

        return bundle.getString(code);

    }

    public static String getMessage(String code) {
        return getMessage(code, null);
    }

}
