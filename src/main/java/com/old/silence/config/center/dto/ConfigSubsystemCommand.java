package com.old.silence.config.center.dto;

/**
 * @author MurrayZhang
 */
public class ConfigSubsystemCommand {

    /**
     * 子系统名称
     */
    private String name;

    /**
     * 子系统编码
     */
    private String code;

    /**
     * 子系统描述
     */
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
