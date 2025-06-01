package com.old.silence.config.center.dto;


import com.old.silence.data.commons.annotation.RelationalQueryProperty;
import com.old.silence.data.commons.converter.Part;

import java.math.BigInteger;



/**
 * @author moryzang
 */
public class ConfigItemQuery  {
    @RelationalQueryProperty(type = Part.Type.SIMPLE_PROPERTY)
    private BigInteger configComponentId;

    public BigInteger getConfigComponentId() {
        return configComponentId;
    }

    public void setConfigComponentId(BigInteger configComponentId) {
        this.configComponentId = configComponentId;
    }
}
