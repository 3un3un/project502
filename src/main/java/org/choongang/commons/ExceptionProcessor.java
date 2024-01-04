package org.choongang.commons;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.choongang.commons.exceptions.CommonException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

// 에러 페이지 인터페이스
public interface ExceptionProcessor {
    @ExceptionHandler(Exception.class)
    default String errorHandler(Exception e, HttpServletResponse response, HttpServletRequest request, Model model) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500번을 기본값으로 정하기
        if(e instanceof CommonException) {
            CommonException commonException = (CommonException) e;
            status = commonException.getStatus();
        }
        response.setStatus(status.value());

        e.printStackTrace();

        model.addAttribute("status", status.value());
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("method", request.getMethod());
        model.addAttribute("message", e.getMessage());

        return "error/common";
    }


}