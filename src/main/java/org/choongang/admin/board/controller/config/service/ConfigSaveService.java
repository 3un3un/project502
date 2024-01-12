package org.choongang.admin.board.controller.config.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.board.controller.config.entities.Configs;
import org.choongang.admin.board.controller.config.repositories.ConfigsRepository;
import org.springframework.stereotype.Service;


// CRUD - 수정, 생성
@Service
@RequiredArgsConstructor
public class ConfigSaveService {
    private final ConfigsRepository repository;

    // code data JSON 문자열로 변환??
    // 수정, 생성 수행 (+데이터 유지)
    public void save(String code, Object data) {
        Configs configs = repository.findById(code)
                .orElseGet(Configs::new); // 데이터가 있으면 가져오고 없으면 비어있는 엔티티 생성

        ObjectMapper om = new ObjectMapper(); // ObjectMapper : 자바 객체 ↔ JSON 문자열 변환 라이브러리
        om.registerModule(new JavaTimeModule()); // 현재 로컬 타임으로 변환

        try {
            String jsonString = om.writeValueAsString(data); // 자바 객체 -> JSON 문자열로 변환
            configs.setData(jsonString);
            configs.setCode(code); // 처음 만든 엔티티에 데이터 저장
            repository.saveAndFlush(configs);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
