package org.choongang.member.service;


import lombok.RequiredArgsConstructor;
import org.choongang.member.controllers.JoinValidator;
import org.choongang.member.controllers.RequestJoin;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final MemberRepository memberRepository;
    private final JoinValidator validator;
    private final PasswordEncoder encoder;


    // 커맨드 객체일 때
    public void process(RequestJoin form, Errors errors) {
        validator.validate(form, errors);
        if(errors.hasErrors()) {
            return; // 종료
        }

        // 비밀번호 BCrypt로 해시화
        String hash = encoder.encode(form.getPassword());

/*        // ModelMapper : 동일한 getter, setter 패턴 → 데이터 치환
        Member member = new ModelMapper().map(form, Member.class); // 알아서 setter() 로 넣어준다.
        member.setPassword(hash);
        process(member);*/

        // setter로
        Member member = new Member();
        member.setEmail(form.getEmail());
        member.setName(form.getName());
        member.setPassword(hash);
        member.setUserId(form.getUserId());

        process(member);

    }

    // 엔티티일 때
    public void process(Member member) {
        memberRepository.saveAndFlush(member);


    }
}
