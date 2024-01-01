package org.choongang.configs;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.interpceptors.CommonInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 인터셉터 관리하는 config
@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final CommonInterceptor commonInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor);
                //.addPathPatterns("/**"); // /** : 모든 경로를 지정할 때는 생략이 가능하다.
    }
}
