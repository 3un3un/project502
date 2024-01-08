package org.choongang.file.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.file.entities.FileInfo;
import org.springframework.stereotype.Service;

import java.io.*;

// CRUD -  다운로드
@Service
@RequiredArgsConstructor
public class FileDownloadService {
    private final FileInfoService infoService;
    private final HttpServletResponse response;

    public void download(Long seq) {
        FileInfo data = infoService.get(seq);
        String filePath = data.getFilePath();
        //String fileName = data.getFileName(); // 원본 파일명

        // 파일명을 2바이트 인코딩으로 변경 (윈도우즈 시스템에서 한글 깨짐 방지)
        String fileName = null;
        try {
            fileName = new String(data.getFileName().getBytes(), "ISO8859_1");
        } catch (UnsupportedEncodingException e) {}

        File file = new File(filePath);

        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            OutputStream out = response.getOutputStream(); // 응답 body에 출력


            // 브라우저가 아닌 파일로 출력하도록 설정 : 응답 헤더인 Content-Disposition
            // url 입력 시 파일 다운로드
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            // 이미지인지 텍스트인지 정확하게 알 수 없음 -> 범용적으로 사용
            response.setHeader("Content-Type", "application/octet-stream");
            response.setIntHeader("Expires", 0); // 파일 용량이 크면 연결 끊음 -> 만료 시간 X

            // 다운로드 할 때 동일한 파일명이지만 다른 내용일 때 다운로드 되도록 하기(캐싱되면 안된다)
            // 동일한 자원이면(이름) 다운로드x -> 캐시
            response.setHeader("Cache-Control", "must-revalidate"); // 1. 최신 브라우저
            response.setHeader("Pragma", "public");// 2. 오래된 브라우저
            response.setHeader("Content-Length", String.valueOf(file.length())); // 파일 용량
            // *요약 -> 응답 헤더로 동작을 바꿔서 다운로드하는 원리이다.

            while(bis.available() > 0) {
                out.write(bis.read());
            }
            out.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
