package com.old.silence.config.center.dto;


import com.old.silence.data.commons.annotation.RelationalQueryProperty;
import com.old.silence.data.commons.converter.Part;

import java.math.BigInteger;


/**
 * @author moryzang
 */
public class ConfigItemQuery  {
    @RelationalQueryProperty(type = Part.Type.SIMPLE_PROPERTY)
    private BigInteger configEnvironmentId;

    @RelationalQueryProperty(type = Part.Type.CONTAINING)
    private String namespaceId;

    @RelationalQueryProperty(type = Part.Type.CONTAINING)
    private BigInteger content;

    public BigInteger getConfigEnvironmentId() {
        return configEnvironmentId;
    }

    public void setConfigEnvironmentId(BigInteger configEnvironmentId) {
        this.configEnvironmentId = configEnvironmentId;
    }

    public String getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(String namespaceId) {
        this.namespaceId = namespaceId;
    }

    public BigInteger getContent() {
        return content;
    }

    public void setContent(BigInteger content) {
        this.content = content;
    }
}
