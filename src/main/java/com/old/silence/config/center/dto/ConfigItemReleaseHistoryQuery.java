package com.old.silence.config.center.dto;


import com.old.silence.data.commons.annotation.RelationalQueryProperty;
import com.old.silence.data.commons.converter.Part;

import java.math.BigInteger;
import java.time.Instant;


/**
 * @author moryzang
 */
public class ConfigItemReleaseHistoryQuery  {
    @RelationalQueryProperty(type = Part.Type.SIMPLE_PROPERTY)
    private BigInteger configItemId;
    @RelationalQueryProperty(name = "createdDate", type = Part.Type.LESS_THAN_EQUAL)
    private Instant createdDateStart;
    @RelationalQueryProperty(name = "createdDate", type = Part.Type.GREATER_THAN_EQUAL)
    private Instant createdDateEnd;

    public BigInteger getConfigItemId() {
        return configItemId;
    }

    public void setConfigItemId(BigInteger configItemId) {
        this.configItemId = configItemId;
    }

    public Instant getCreatedDateStart() {
        return createdDateStart;
    }

    public void setCreatedDateStart(Instant createdDateStart) {
        this.createdDateStart = createdDateStart;
    }

    public Instant getCreatedDateEnd() {
        return createdDateEnd;
    }

    public void setCreatedDateEnd(Instant createdDateEnd) {
        this.createdDateEnd = createdDateEnd;
    }
}
