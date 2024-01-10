package org.choongang.admin.config;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.transaction.Transactional;
import org.choongang.admin.board.controller.config.controllers.BasicConfig;
import org.choongang.admin.board.controller.config.service.ConfigInfoService;
import org.choongang.admin.board.controller.config.service.ConfigSaveService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;
import java.util.Optional;

@SpringBootTest
@TestPropertySource(properties = "spring.profiles.active=test")
@Transactional
public class ConfigSaveTest {

    @Autowired
    private ConfigInfoService infoService;
    @Autowired
    private ConfigSaveService saveService;
    @Test
    @DisplayName("BasicConfig로 생성된 객체가 JSON으로 저장되는지 테스트")
    void saveTest() {
        BasicConfig config = new BasicConfig();
        config.setSiteTitle("사이트 제목");
        config.setSiteDescription("사이트 설명");
        config.setSiteKeywords("사이트 키워드");
        config.setCssJsVersion(1);
        config.setJoinTerms("회원가입 약관");

        saveService.save("basic", config);

        // 1. 클래스로 조회
        BasicConfig config2 = infoService.get("basic", BasicConfig.class).get();
        System.out.println("config2 = " + config2);

        // 2. Map(+콜렉션, 복잡/중첩)으로 가지고 오고 싶을 때 Class X, TypeReference O
        Optional<Map<String, String>> opt = infoService.get("basic", new TypeReference<>() {
        });
        System.out.println("opt = " + opt.get());

    }

}
