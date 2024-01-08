package org.choongang.member.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.Utils;
import org.choongang.member.MemberUtil;
import org.choongang.member.service.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController implements ExceptionProcessor {

    private final Utils utils;
//    private final JoinValidator joinValidator;
    private final JoinService joinService;
    private final MemberUtil memberUtil;

    @GetMapping("/join")
    public String join(@ModelAttribute RequestJoin form, Model model) {
        commonProcess("join", model);

        return utils.tpl("member/join");
    }




    @PostMapping("/join")
    public String joinPs(@Valid RequestJoin form, Errors errors,Model model) {
        commonProcess("join", model);

        // joinValidator.validate(form, errors);
        joinService.process(form, errors);

        if (errors.hasErrors()) {
            return utils.tpl("member/join");
        }


        return "redirect:/member/login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        commonProcess("login", model);

        return utils.tpl("member/login");
    }


    // 모드 값에 따라 공통 처리하기
    // ex) GET: join POST: join = 같은 모드
    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "join";
        String pageTitle = Utils.getMessage("회원가입", "commons");

        List<String> addCommonScript = new ArrayList<>(); // 공통 자바스크립트
        List<String> addScript = new ArrayList<>(); // 프론트(+모바일) 자바스크립트

        if (mode.equals("login")) {
            pageTitle = Utils.getMessage("로그인", "commons");

            // 회원가입 시 프로필
        } else if(mode.equals("join")) {
            addCommonScript.add("fileManager");
            addScript.add("member/form");
        }

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
    }




/*  1번 Principal
    @ResponseBody
    @GetMapping("/info")
    public void info(Principal principal) {
        String username = principal.getName();
        System.out.printf("username=%s%n", username);
    }*/

/*  3번 @AuthenticationPrincipal
    @ResponseBody
    @GetMapping("/info")
    public void info(@AuthenticationPrincipal MemberInfo memberInfo) {
        System.out.println("memberInfo = " + memberInfo);
    }*/


    // **4번 Authentication
/*    @ResponseBody
    @GetMapping("/info")
    public void info() {
        MemberInfo memberInfo = (MemberInfo) SecurityContextHolder
                                        .getContext()
                                        .getAuthentication()
                                        .getPrincipal();

        System.out.println("memberInfo = " + memberInfo);
    }*/

/*    @ResponseBody
    @GetMapping("/info")
    public void info() {
        if(memberUtil.isLogin()) {
            Member member = memberUtil.getMember();
            System.out.println("member = " + member);
        } else {
            System.out.println("미로그인 상태...");
        }
    }*/


}
