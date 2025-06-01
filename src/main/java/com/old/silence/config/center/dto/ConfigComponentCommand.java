package com.old.silence.config.center.dto;

import java.math.BigInteger;

/**
 * @author MurrayZhang
 */
public class ConfigComponentCommand {

    /**
     * 组件名称
     */
    private String name;

    /**
     * 组件编码
     */
    private String code;

    /**
     * 所属子系统ID
     */
    private BigInteger subsystemId;

    /**
     * 组件描述
     */
    private String description;

    /**
     * 组件状态
     */
    private Boolean status;

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

    public BigInteger getSubsystemId() {
        return subsystemId;
    }

    public void setSubsystemId(BigInteger subsystemId) {
        this.subsystemId = subsystemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
