package com.old.silence.config.center.dto;


import jakarta.validation.constraints.NotBlank;

public class ConfigItemContentCommand{

    /**
     * 配置项值
     */
    @NotBlank
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}