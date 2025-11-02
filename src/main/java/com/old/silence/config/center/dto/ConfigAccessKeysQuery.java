package com.old.silence.config.center.dto;

import com.old.silence.data.commons.annotation.RelationalQueryProperty;
import com.old.silence.data.commons.converter.Part;

/**
 * @author moryzang
 */
public class ConfigAccessKeysQuery {

    @RelationalQueryProperty(type = Part.Type.SIMPLE_PROPERTY)
    private String componentCode;

    @RelationalQueryProperty(type = Part.Type.SIMPLE_PROPERTY)
    private Boolean enabled;

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
