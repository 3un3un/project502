package org.choongang.admin.config.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.service.ConfigInfoService;
import org.choongang.admin.config.service.ConfigSaveService;
import org.choongang.commons.ExceptionProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/config")
@RequiredArgsConstructor
public class BasicConfigsController implements ExceptionProcessor {

    private final ConfigSaveService saveService;
    private final ConfigInfoService infoService;



    @ModelAttribute("menuCode") // 메소드가 반환하는 객체를 Model를 통해 뷰에 전달 된다.
    public String getMenuCode() {
        return "config";
    }


    // 메인제목 - 부제목
    @ModelAttribute("pageTitle")
    public String getPageTitle() {
        return "기본설정";
    }

    @GetMapping
    public String index(Model model) {
        // 양식 가져오기 (없으면 새로 생성)
        BasicConfig config = infoService.get("basic", BasicConfig.class).orElseGet(BasicConfig::new);

        model.addAttribute("basicConfig", config);

        return "admin/config/basic";
    }

    @PostMapping
    public String save(BasicConfig config, Model model) {
        // 양식 넘기기(페이지는 유지)
        saveService.save("basic", config);
        model.addAttribute("message", "저장되었습니다.");

        return "admin/config/basic";
    }

}
