package com.old.silence.config.center.dto;

import jakarta.validation.constraints.NotNull;
import com.old.silence.config.center.enums.ConflictStrategy;

import java.math.BigInteger;
import java.util.List;

/**
 * @author moryzang
 */
public class ConfigNamespaceSyncCommand {

    @NotNull
    private BigInteger sourceConfigItemId;

    @NotNull
    private BigInteger targetEnvironmentId;

    private List<String> targetNamespaceIds;

    @NotNull
    private ConflictStrategy conflictStrategy;

    public BigInteger getSourceConfigItemId() {
        return sourceConfigItemId;
    }

    public void setSourceConfigItemId(BigInteger sourceConfigItemId) {
        this.sourceConfigItemId = sourceConfigItemId;
    }

    public BigInteger getTargetEnvironmentId() {
        return targetEnvironmentId;
    }

    public void setTargetEnvironmentId(BigInteger targetEnvironmentId) {
        this.targetEnvironmentId = targetEnvironmentId;
    }

    public List<String> getTargetNamespaceIds() {
        return targetNamespaceIds;
    }

    public void setTargetNamespaceIds(List<String> targetNamespaceIds) {
        this.targetNamespaceIds = targetNamespaceIds;
    }

    public ConflictStrategy getConflictStrategy() {
        return conflictStrategy;
    }

    public void setConflictStrategy(ConflictStrategy conflictStrategy) {
        this.conflictStrategy = conflictStrategy;
    }
}
