package com.old.silence.config.center.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import com.old.silence.config.center.enums.CloneMode;

import java.math.BigInteger;
import java.util.List;

/**
 * @author moryzang
 */
public class ConfigNamespaceCloneCommand {

    @NotNull
    private BigInteger sourceEnvironmentId;

    @NotEmpty
    private List<BigInteger> targetEnvironmentIds;

    @NotNull
    private CloneMode cloneMode;

    public BigInteger getSourceEnvironmentId() {
        return sourceEnvironmentId;
    }

    public void setSourceEnvironmentId(BigInteger sourceEnvironmentId) {
        this.sourceEnvironmentId = sourceEnvironmentId;
    }

    public List<BigInteger> getTargetEnvironmentIds() {
        return targetEnvironmentIds;
    }

    public void setTargetEnvironmentIds(List<BigInteger> targetEnvironmentIds) {
        this.targetEnvironmentIds = targetEnvironmentIds;
    }

    public CloneMode getCloneMode() {
        return cloneMode;
    }

    public void setCloneMode(CloneMode cloneMode) {
        this.cloneMode = cloneMode;
    }
}
