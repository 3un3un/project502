package org.choongang.commons;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.board.controller.config.controllers.BasicConfig;
import org.choongang.admin.board.controller.config.service.ConfigInfoService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(value="org.choongang")
@RequiredArgsConstructor
public class BasicConfigAdvice {
    private final ConfigInfoService infoService;


    @ModelAttribute("siteConfig") // @ControllerAdvice 범위 모든 페이지에서 공유
    public BasicConfig getBasicConfig() {


        BasicConfig config = infoService.get("basic", BasicConfig.class)
                .orElseGet(BasicConfig::new);

        /*Optional<Map<String, String>> opt = infoService.get("basic", new TypeReference<>() {});

        Map<String, String> config = opt.orElseGet(() -> new HashMap<>());*/


        return config;
    }

/*    public String get(String key) {


    }*/

}
