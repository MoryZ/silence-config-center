package com.old.silence.config.center.dto;


import com.old.silence.data.commons.annotation.RelationalQueryProperty;
import com.old.silence.data.commons.converter.Part;
import com.old.silence.config.center.enums.ConfigEnvironmentType;

import java.math.BigInteger;

/**
 * @author moryzang
 */
public class ConfigEnvironmentQuery  {
    @RelationalQueryProperty(type = Part.Type.SIMPLE_PROPERTY)
    private BigInteger configComponentId;

    @RelationalQueryProperty(type = Part.Type.SIMPLE_PROPERTY)
    private ConfigEnvironmentType envType;

    public BigInteger getConfigComponentId() {
        return configComponentId;
    }

    public void setConfigComponentId(BigInteger configComponentId) {
        this.configComponentId = configComponentId;
    }

    public ConfigEnvironmentType getEnvType() {
        return envType;
    }

    public void setEnvType(ConfigEnvironmentType envType) {
        this.envType = envType;
    }
}
