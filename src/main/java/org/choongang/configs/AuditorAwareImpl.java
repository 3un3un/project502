package org.choongang.configs;

import org.choongang.member.service.MemberInfo;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String > {

    @Override
    public Optional<String> getCurrentAuditor() {
        String userId = null;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        /**
         * getPrincipal()
         * 로그인 상태일 때 : UserDetails 구현 객체 (MemberInfo)
         * 미로그인 상태일 때 : null X, String 형태의 anonymousUser
         */
        if(auth != null && auth.getPrincipal() instanceof MemberInfo) { // 로그인 시
            MemberInfo memberInfo = (MemberInfo) auth.getPrincipal();
            userId = memberInfo.getUserId();
        }

        return Optional.ofNullable(userId); // 로그인 시 사용자 아이디 / x null 반환
    }
}
