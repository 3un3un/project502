package org.choongang.admin.board.controller.config.controllers;

import lombok.Data;

@Data
public class BasicConfig {
    private String siteTitle = "";
    private String siteDescription = "";
    private String siteKeywords = "";
    private int cssJsVersion = 1;
    private String joinTerms = "";
    private String thumbSize = "";

}
