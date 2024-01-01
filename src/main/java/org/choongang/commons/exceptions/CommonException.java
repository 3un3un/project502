package org.choongang.commons.exceptions;

import org.springframework.http.HttpStatus;

public class CommonException extends RuntimeException{
    private HttpStatus status; // 응답코드

    public CommonException(String message, HttpStatus status) {
        super(message);
        this.status = status;

    }

    public HttpStatus getStatus() {
        return status; // 응답코드 가져오기
    }

}
