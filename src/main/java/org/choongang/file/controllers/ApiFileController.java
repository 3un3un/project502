package org.choongang.file.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionRestProcessor;
import org.choongang.commons.rests.JSONData;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.service.FileDeleteService;
import org.choongang.file.service.FileUploadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class ApiFileController implements ExceptionRestProcessor {

    private final FileUploadService uploadService;
    private final FileDeleteService deleteService;

    @PostMapping
    public JSONData<List<FileInfo>> upload(@RequestParam("file") MultipartFile[] files,
                                           @RequestParam(name="gid", required = false) String gid,
                                           @RequestParam(name="location", required = false) String location,
                                           // 이미지만 업로드 가능
                                           @RequestParam(name="imageOnly", required = false) boolean imageOnly) {

        List<FileInfo> uploadedFiles = uploadService.upload(files, gid, location, imageOnly);
        return new JSONData<>(uploadedFiles);

    }


    // 어드민만 - 삭제
    @GetMapping("/{seq}")
    public void delete(@PathVariable("seq") Long seq) { // 쿼리스트링
        deleteService.delete(seq);

    }
}
