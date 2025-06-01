package com.old.silence.config.center.dto;


import com.old.silence.data.commons.annotation.RelationalQueryProperty;
import com.old.silence.data.commons.converter.Part;

import java.math.BigInteger;

/**
 * @author moryzang
 */
public class ConfigComponentQuery  {
    @RelationalQueryProperty(type = Part.Type.SIMPLE_PROPERTY)
    private BigInteger subsystemId;

    public BigInteger getSubsystemId() {
        return subsystemId;
    }

    public void setSubsystemId(BigInteger subsystemId) {
        this.subsystemId = subsystemId;
    }
}
