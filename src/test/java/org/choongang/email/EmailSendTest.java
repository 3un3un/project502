package org.choongang.email;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

/*
@SpringBootTest
public class EmailSendTest {
    @Autowired
    private EmailSendService emailSendService;

    @Test
    void sendTest() {
        EmailMessage message = new EmailMessage("abc1987042@naver.com", "제목2...", "내용2...");
        boolean success = emailSendService.sendMail(message);

        assertTrue(success);
    }
*/


/*   @Test
    @DisplayName("템플릿 형태로 전송 테스트")
    void sendWithTplTest() {
        EmailMessage message = new EmailMessage("yonggyo00@kakao.com", "제목...", "내용...");
        Map<String,Object> tplData = new HashMap<>();
        tplData.put("authNum", "123456");
        boolean success = emailSendService.sendMail(message, "auth", tplData);

        assertTrue(success);
    }
}*/
