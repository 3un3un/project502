package org.choongang.file.service;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.Utils;
import org.choongang.commons.exceptions.UnAuthorizedException;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.repositories.FileInfoRepository;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;

// CRUD -  삭제
// 관리자 여부 확인 후 삭제
@Service
@RequiredArgsConstructor
public class FileDeleteService {

    private final FileInfoService infoService;

    private final MemberUtil memberUtil;

    private final FileInfoRepository repository;

    public void delete(Long seq) {
        FileInfo data = infoService.get(seq);
        // 파일 삭제 - 관리자 여부 체크
        Member member = memberUtil.getMember();
        String createdBy = data.getCreatedBy();

        if(!memberUtil.isLogin() || (!memberUtil.isAdmin() && StringUtils.hasText(createdBy)
        && !createdBy.equals(member.getUserId()))) {
            throw new UnAuthorizedException(Utils.getMessage("Not.your.file", "errors"));

        }

        File file = new File(data.getFilePath());
        if(file.exists()) file.delete();

        // 썸네일 삭제
        List<String> thumbsPath = data.getThumsPath();
        if (thumbsPath != null) {
            for (String path : thumbsPath) {
                File thumbFile = new File(path);
                if (thumbFile.exists()) thumbFile.delete();
            }
        }

        // 파일 데이터 삭제
        repository.delete(data);
        repository.flush();




    }


}
