package com.old.silence.config.center.dto;

import com.old.silence.config.center.enums.ConfigItemFormatType;
import com.old.silence.config.center.enums.ConfigItemType;
import com.old.silence.config.center.enums.NameSpaceStatus;
import com.old.silence.config.center.enums.OperationType;

import java.math.BigInteger;

/**
 * @author MurrayZhang
 */
public class ConfigItemCommand {
    private BigInteger configEnvironmentId;

    private String namespaceId;

    private NameSpaceStatus namespaceStatus;

    private ConfigItemFormatType formatType;

    /**
     * 配置项类型
     */
    private ConfigItemType type;

    /**
     * 配置项值
     */
    private String oldContent;

    /**
     * 配置项值
     */
    private String content;

    private String md5;

    private OperationType operationType;

    public BigInteger getConfigEnvironmentId() {
        return configEnvironmentId;
    }

    public void setConfigEnvironmentId(BigInteger configEnvironmentId) {
        this.configEnvironmentId = configEnvironmentId;
    }

    public String getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(String namespaceId) {
        this.namespaceId = namespaceId;
    }

    public NameSpaceStatus getNamespaceStatus() {
        return namespaceStatus;
    }

    public void setNamespaceStatus(NameSpaceStatus namespaceStatus) {
        this.namespaceStatus = namespaceStatus;
    }

    public ConfigItemFormatType getFormatType() {
        return formatType;
    }

    public void setFormatType(ConfigItemFormatType formatType) {
        this.formatType = formatType;
    }

    public ConfigItemType getType() {
        return type;
    }

    public void setType(ConfigItemType type) {
        this.type = type;
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

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }
}
