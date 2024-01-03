package org.choongang.member.entities;


import jakarta.persistence.*;
import lombok.Data;
import org.choongang.member.Authority;


@Data
@Entity
@Table(indexes = @Index(name="uq_member_authority",
                columnList = "member_seq, authority",
                unique = true)) // unique : 하나의 회원은 여러가지 권한을 가질 수 있음(중복은 x)
public class Authorities {
    @Id @GeneratedValue
    private Long seq;

    // 회원은 1개, 권한은 여러 개
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_seq")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(length=15, nullable = false)
    private Authority authority;


}
