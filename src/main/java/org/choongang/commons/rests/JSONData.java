package org.choongang.commons.rests;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class JSONData<T> {
    private HttpStatus status = HttpStatus.OK;
    private boolean success = true;

    @NonNull
    private T data; // 매개변수 생성자 만들기
    private String message;
}
