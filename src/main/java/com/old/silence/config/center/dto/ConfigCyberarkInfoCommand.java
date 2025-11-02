package com.old.silence.config.center.dto;


/**
 * @author moryzang
 */
public class ConfigCyberarkInfoCommand {
    /**
     * 访问密钥
     */
    private String cyberarkObject;

    /**
     * 访问私钥
     */
    private String encryptedValue;

    /**
     * 所属组件编码
     */
    private String componentCode;

    private String safe;

    private String folder;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否启用
     */
    private Boolean enabled;

    public String getCyberarkObject() {
        return cyberarkObject;
    }

    public void setCyberarkObject(String cyberarkObject) {
        this.cyberarkObject = cyberarkObject;
    }

    public String getEncryptedValue() {
        return encryptedValue;
    }

    public void setEncryptedValue(String encryptedValue) {
        this.encryptedValue = encryptedValue;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public String getSafe() {
        return safe;
    }

    public void setSafe(String safe) {
        this.safe = safe;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
