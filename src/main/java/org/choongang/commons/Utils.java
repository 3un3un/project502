package org.choongang.commons;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.controllers.BasicConfig;
import org.choongang.file.service.FileInfoService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class Utils {

    private final HttpServletRequest request;

    private final HttpSession session;

    private final FileInfoService fileInfoService;



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


    /**
     * \n 또는 \r\n (줄개행 문자) -> <br> 태그로
     * @param str
     * @return
     */
    // 줄개행 문자  <br> 태그로 변경하는 기능
    public String nl2br(String str) {
        str = str.replaceAll("\\n", "<br>")
                .replaceAll("\\r", "");
        return str;
    }

    /**
     * 썸네일 이미지 사이즈 설정
     *
     * @return
     */
    public List<int[]> getThumbsSize() {
        BasicConfig config = (BasicConfig) request.getAttribute("siteConfig");
        String thumbSize = config.getThumbSize();
        String[] thumbsSize = thumbSize.split("\\n"); // 자르기

/*        Arrays.stream(thumbsSize).map(s -> s.replaceAll("\\r", ""))
                .map(s -> s.toUpperCase().split("X"));*/

        List<int[]> data = Arrays.stream(thumbsSize)
                .filter(StringUtils::hasText) // 공백 제거
                .map(s -> s.replaceAll("\\s+", ""))
                .map(this::toConvert).toList();

        return data;
    }

    private int[] toConvert(String size) {

        size = size.trim(); // 공백 제거

        int[] data = Arrays.stream(size.replaceAll("\\r", "")
                .toUpperCase().split("X"))
                .mapToInt(Integer::parseInt).toArray();

        return data;
    }

    // 썸네일 이미지 태그 출력 -> 타임리프
    public String printThumb(long seq, int width, int height, String className) {
        String[] data = fileInfoService.getThumb(seq, width, height);
        if (data != null) {
         String cls = StringUtils.hasText(className) ? " class'" + className + "'" : "";
         String image = String.format("<img src='%s'%s>", data[1], cls);
         return image;
        }
        return "";

    }

    public String printThumb(long seq, int width, int height) {
        return printThumb(seq, width, height, null);
    }

}
