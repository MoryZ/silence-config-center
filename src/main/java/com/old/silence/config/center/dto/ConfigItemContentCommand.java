package com.old.silence.config.center.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.old.silence.config.center.enums.OperationType;

public class ConfigItemContentCommand{

    /**
     * 配置项值
     */
    @NotBlank
    private String content;

    @NotNull
    private OperationType operationType;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }
}