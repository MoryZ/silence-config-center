package com.old.silence.config.center.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.old.silence.config.center.domain.model.ConfigItem;
import com.old.silence.config.center.enums.ConfigItemFormatType;
import com.old.silence.config.center.enums.ConfigItemType;
import com.old.silence.config.center.enums.NameSpaceStatus;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;

/**
 * @author MurrayZhang
 */
public class ConfigItemVo {

    private final List<String> ips;

    private final ConfigItem configItem;

    public ConfigItemVo(ConfigItem configItem, List<String> ips) {
        this.ips = ips;
        this.configItem = configItem;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public BigInteger getId() {
        return configItem.getId();
    }

    public List<String> getIps() {
        return ips;
    }

    public BigInteger getConfigEnvironmentId() {
        return configItem.getConfigEnvironmentId();
    }

    public String getNamespaceId() {
        return configItem.getNamespaceId();
    }

    public NameSpaceStatus getNamespaceStatus() {
        return configItem.getNamespaceStatus();
    }

    public ConfigItemFormatType getFormatType() {
        return configItem.getFormatType();
    }

    public ConfigItemType getType() {
        return configItem.getType();
    }

    public String getOldContent() {
        return configItem.getOldContent();
    }

    public String getContent() {
        return configItem.getContent();
    }

    public String getMd5() {
        return configItem.getMd5();
    }

    public String getUpdatedBy() {
        return configItem.getUpdatedBy();
    }

    public Instant getUpdatedDate() {
        return configItem.getUpdatedDate();
    }

}
