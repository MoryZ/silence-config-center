package com.old.silence.config.center.dto;

import com.old.silence.config.center.enums.ConfigEnvironmentType;

import java.math.BigInteger;

/**
 * @author MurrayZhang
 */
public class ConfigEnvironmentCommand {

    private BigInteger configComponentId;

    /**
     * 配置项值
     */
    private String name;

    /**
     * 环境名称
     */
    private String displayName;

    /**
     * 配置项类型
     */
    private ConfigEnvironmentType envType;

    /**
     * 配置项状态
     */
    private Boolean display;

    public BigInteger getConfigComponentId() {
        return configComponentId;
    }

    public void setConfigComponentId(BigInteger configComponentId) {
        this.configComponentId = configComponentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ConfigEnvironmentType getEnvType() {
        return envType;
    }

    public void setEnvType(ConfigEnvironmentType envType) {
        this.envType = envType;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }
}
