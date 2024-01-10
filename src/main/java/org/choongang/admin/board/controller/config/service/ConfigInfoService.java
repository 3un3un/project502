package org.choongang.admin.board.controller.config.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.board.controller.config.entities.Configs;
import org.choongang.admin.board.controller.config.repositories.ConfigsRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

// CRUD - 조회
@Service
@RequiredArgsConstructor
public class ConfigInfoService {

    private final ConfigsRepository repository;

    // 1. 클래스로 조회
    public <T> Optional<T> get(String code, Class<T> clazz) {
        return get(code, clazz, null);
    }

    // 2. Map으로 가지고 오고 싶을 때 Class X, TypeReference(.jakson...) O
    public <T> Optional<T> get(String code, TypeReference<T> typeReference) {
        return get(code, null, typeReference);

    }

    // 조회 시에는 형변환이 필요 -> 지네릭스 사용
    public <T> Optional<T> get(String code, Class<T> clazz, TypeReference<T> typeReference) {
        Configs config = repository.findById(code).orElse(null);
        if(config == null || !StringUtils.hasText(config.getData())) {
            return Optional.ofNullable(null);
        }

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());

        String jsonString = config.getData();
        try {
            T data = null;
            if(clazz == null) { // TypeReference로 처리
                data = om.readValue(jsonString, new TypeReference<T>() {}); // JSON 문자열 -> 자바 객체로 변환
            } else { // Class로 처리
                data = om.readValue(jsonString, clazz);
            }
            return Optional.ofNullable(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Optional.ofNullable(null);
        }

    }
}
