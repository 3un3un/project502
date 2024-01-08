package org.choongang.member.controllers;




import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;


// 커맨드 객체
@Data
public class RequestJoin {

    private String gid = UUID.randomUUID().toString(); // UNIQUE ID 만들기

    @NotBlank @Email
    private String email;

    @NotBlank
    @Size(min=6)
    private String userId;

    @NotBlank
    @Size(min=8)
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    private String name;

    @AssertTrue // @NotBlank : true/false
    private boolean agree;

}
