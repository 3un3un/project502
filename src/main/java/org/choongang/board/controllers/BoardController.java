package org.choongang.board.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.board.entities.BoardData;
import org.choongang.board.repositories.BoardDataRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardDataRepository boardDataRepository;

    @ResponseBody
    @GetMapping("/test")
    public void test() {
        BoardData data = boardDataRepository.findById(1L).orElse(null);
        data.setSubject("(수정)제목");
        boardDataRepository.saveAndFlush(data);
/*        BoardData data = new BoardData();
        data.setSubject("제목");
        data.setContent("내용");
        boardDataRepository.saveAndFlush(data);*/

    }
    @GetMapping("/test2")
    @PreAuthorize("hasAuthority('ADMIN')") // 자세하게(반환값..)
//    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})//간단하게
    public void test2() {
        System.out.println("test2!!!");
    }
}
