package com.old.silence.config.center.dto;

import com.old.silence.config.center.enums.OperationType;

import java.math.BigInteger;

/**
 * @author MurrayZhang
 */
public class ConfigItemHistoryCommand {


    /**
     * 配置项名称
     */
    private BigInteger configItemId;

    /**
     * 修改前的值
     */
    private String oldContent;

    /**
     * 修改后的值
     */
    private String content;

    /**
     * 修改类型（CREATE/UPDATE/DELETE）
     */
    private OperationType operationType;

    public BigInteger getConfigItemId() {
        return configItemId;
    }

    public void setConfigItemId(BigInteger configItemId) {
        this.configItemId = configItemId;
    }

    public String getOldContent() {
        return oldContent;
    }

    public void setOldContent(String oldContent) {
        this.oldContent = oldContent;
    }

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
