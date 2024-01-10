package org.choongang.admin.board.controller.config.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Entity
@Data
public class Configs {
    @Id
    @Column(length = 60)
    private String code;

    @Lob
    private String data;
}
