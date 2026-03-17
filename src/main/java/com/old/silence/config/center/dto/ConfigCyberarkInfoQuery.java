package com.old.silence.config.center.dto;


import com.old.silence.data.commons.annotation.RelationalQueryProperty;
import com.old.silence.data.commons.converter.Part;

/**
 * @author moryzang
 */
public class ConfigCyberarkInfoQuery {
    /**
     * CyberarkObject
     */
    @RelationalQueryProperty(type = Part.Type.CONTAINING)
    private String cyberarkObject;

    /**
     * 所属组件编码
     */
    @RelationalQueryProperty(type = Part.Type.SIMPLE_PROPERTY)
    private String componentCode;

    /**
     * 是否启用
     */
    @RelationalQueryProperty(type = Part.Type.SIMPLE_PROPERTY)
    private Boolean enabled;

    public String getCyberarkObject() {
        return cyberarkObject;
    }

    public void setCyberarkObject(String cyberarkObject) {
        this.cyberarkObject = cyberarkObject;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
