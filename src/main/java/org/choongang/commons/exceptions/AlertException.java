package org.choongang.commons.exceptions;

import org.springframework.http.HttpStatus;


// 예외 발생 시 alert 띄어주기
public class AlertException extends CommonException {

    public AlertException(String message, HttpStatus status) {
        super(message, status);
    }
}
