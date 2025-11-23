package com.old.silence.config.center.dto;

import jakarta.validation.constraints.NotNull;
import com.old.silence.config.center.enums.CloneMode;

import java.math.BigInteger;

/**
 * @author moryzang
 */
public class ConfigNamespaceCloneCommand {

    @NotNull
    private BigInteger sourceEnvironmentId;

    @NotNull
    private BigInteger targetEnvironmentId;

    @NotNull
    private CloneMode cloneMode;

    public BigInteger getSourceEnvironmentId() {
        return sourceEnvironmentId;
    }

    public void setSourceEnvironmentId(BigInteger sourceEnvironmentId) {
        this.sourceEnvironmentId = sourceEnvironmentId;
    }

    public BigInteger getTargetEnvironmentId() {
        return targetEnvironmentId;
    }

    public void setTargetEnvironmentId(BigInteger targetEnvironmentId) {
        this.targetEnvironmentId = targetEnvironmentId;
    }

    public CloneMode getCloneMode() {
        return cloneMode;
    }

    public void setCloneMode(CloneMode cloneMode) {
        this.cloneMode = cloneMode;
    }
}
