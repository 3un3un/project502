package org.choongang.member.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.Utils;
import org.choongang.member.service.JoinService;
import org.choongang.member.service.MemberInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController implements ExceptionProcessor {

    private final Utils utils;

//    private final JoinValidator joinValidator;

    private final JoinService joinService;

    @GetMapping("/join")
    public String join(@ModelAttribute RequestJoin form) {


        // 모바일여부 체크 후 이동
        return utils.tpl("member/join");
    }

    @PostMapping("/join")
    public String joinPs(@Valid RequestJoin form, Errors errors) {

        // joinValidator.validate(form, errors);
        joinService.process(form, errors);

        if(errors.hasErrors()) {
            return utils.tpl("member/join");
        }

        return "redirect:/member/login";

    }

    @GetMapping("/login")
    public String login() {

        return utils.tpl("/member/login");
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
    @ResponseBody
    @GetMapping("/info")
    public void info() {
        MemberInfo memberInfo = (MemberInfo) SecurityContextHolder
                                        .getContext()
                                        .getAuthentication()
                                        .getPrincipal();

        System.out.println("memberInfo = " + memberInfo);
    }


}
