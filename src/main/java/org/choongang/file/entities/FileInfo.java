package org.choongang.file.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.commons.entites.BaseMember;

import java.util.UUID;

@Entity
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(indexes = {
        @Index(name = "idx_fInfo_gid", columnList = "gid"),
        @Index(name = "idx_fInfo_gid_loc", columnList = "gid,location")
})
public class FileInfo extends BaseMember {
    @Id @GeneratedValue
    private Long seq; // 파일 등록 번호, 서버에 업로드 하는 파일명 기준(확장자 + seq)

    @Column(length = 65, nullable = false) // UNIQUE 키값
    private String gid = UUID.randomUUID().toString(); // 랜덤 아이디 생성

    @Column(length = 65)
    private String location;

    @Column(length = 80)
    private String fileName; // 원본 파일명

    @Column(length = 30)
    private String extension; // 확장자

    private boolean done;








}
