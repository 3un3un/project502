package org.choongang.member.service;


import lombok.RequiredArgsConstructor;
import org.choongang.member.entities.Authorities;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

// UserDetailsService 인터페이스 - service
// 멤버 조회

@Service
@RequiredArgsConstructor
public class
MemberInfoService implements UserDetailsService {
    private final MemberRepository memberRepository;


    // 회원 정보 조회 (인증할 때 등)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username) // 이메일 체크
                .orElseGet(() -> memberRepository.findByUserId(username)// 이메일이 없으면 아이디로 체크
                        .orElseThrow(() -> new UsernameNotFoundException(username))); // 둘 다 없으면 예외 처리

        // 권한 가져오기
        List<SimpleGrantedAuthority> authorities = null;
        List<Authorities> tmp = member.getAuthorities();
        if(tmp != null) {
            authorities = tmp.stream()
                    .map(s -> new SimpleGrantedAuthority(s.getAuthority().name())) // 권한을 문자열로
                    .toList(); // 리스트로 변환
        }

        // 반환하면 알아서 로그인
        return MemberInfo.builder()
                .email(member.getEmail())
                .userId(member.getUserId())
                .password(member.getPassword())
                .member(member)
                .authorities(authorities)
                .build();
    }






}
