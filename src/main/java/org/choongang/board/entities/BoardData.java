package org.choongang.board.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.choongang.commons.entites.BaseMember;

@Entity
@Data
public class BoardData extends BaseMember {

    @Id @GeneratedValue
    private Long seq;

    private String subject;

    private String content;


}
