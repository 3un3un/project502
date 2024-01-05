package org.choongang.commons.exceptions;

import org.springframework.http.HttpStatus;

// 예외 발생 시 alert 띄어주고 전 페이지로 이동
public class AlertBackException extends AlertException{
    public AlertBackException(String message, HttpStatus status) {
        super(message, status);
    }
}
