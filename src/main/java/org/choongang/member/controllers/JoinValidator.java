package org.choongang.member.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.validators.PasswordValidator;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class JoinValidator implements Validator, PasswordValidator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestJoin.class); // 검증 대상 설정
    }

    @Override
    public void validate(Object target, Errors errors) {
        /** 추가 검증이 필요
         * 1. 이메일, 아이디 중복 여부 체크
         * 2. 비밀번호 복잡성 체크 - 대소문자 1개 각각 포함, 숫자 1개 이상 포함, 특수문자 1개 이상 포함
         * 3. 비밀번호, 비밀번호 확인 일치 여부 체크
         */

        RequestJoin form = (RequestJoin) target;
        String email = form.getEmail();
        String userId = form.getUserId();
        String password = form.getPassword();
        String confirmPassword = form.getConfirmPassword();

         // 1. 이메일, 아이디 중복 여부 체크
        if(StringUtils.hasText(email) && memberRepository.existsByEmail(email)) {
            errors.rejectValue("email", "Duplicated");

        }

        if(StringUtils.hasText(userId) && memberRepository.existsByUserId(userId)) {
            errors.rejectValue("userId", "Duplicated");
        }


        // 2. 비밀번호 복잡성 체크 - 대소문자 1개 각각 포함, 숫자 1개 이상 포함, 특수문자 1개 이상 포함
        // PasswordValidator 인터페이스에 만듦
        if(StringUtils.hasText(password) &&
                (!alphaCheck(password, true) || !numberCheck(password)
                   || !specialCharsCheck(password))) {
            errors.rejectValue("password", "Complexity");

        }



        // 3. 비밀번호, 비밀번호 확인 일치 여부 체크
        if(StringUtils.hasText(password) && StringUtils.hasText(confirmPassword)
            && !password.equals(confirmPassword)) {
            // 존재는 하는데 일치하지 x

            errors.rejectValue("confirmPassword", "Mismatch.password");

        }


    }
}
