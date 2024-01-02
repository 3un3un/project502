package org.choongang.member.repositories;

import org.choongang.member.entities.Member;
import org.choongang.member.entities.QMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

// db 접근
// jparepository : dao 역할 해줌

// JpaRepository : findAll, findBy... 다양한 조회는 불가능 -> querydsl 사용한다.
// QuerydslPredicateExecutor : find.. 매개변수로 Predicate 인터페이스가 추가되어 있어 조회할 때 유리하다.
// Predicate 인터페이스 : 반환값이 Predicate, or/and 메서드 ...

public interface MemberRepository extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member> {


    // 이메일 없을 때를 대비하여 Optional로 사용 (없을 때 메세지)
    // 이메일 체크 -> 아이디 체크
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUserId(String userId);

    default boolean existsByEmail(String email) {
        QMember member = QMember.member;

        return exists(member.email.eq(email));

    }

    default boolean existsByUserId(String userId) {
        QMember member = QMember.member;

        return exists(member.userId.eq(userId));

    }


}
