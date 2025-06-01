package com.old.silence.config.center.dto;

import com.old.silence.config.center.enums.ReleaseType;

import java.math.BigInteger;

/**
 * @author MurrayZhang
 */
public class ConfigItemReleaseHistoryCommand {

    /**
     * 发布名称
     */
    private String releaseName;

    /**
     * 配置项名称
     */
    private BigInteger configItemId;

    /**
     * 修改前的值
     */
    private String oldContent;

    /**
     * 修改后的值
     */
    private String content;

    /**
     * 发布类型（普通发布/灰度发布）
     */
    private ReleaseType releaseType;

    public String getReleaseName() {
        return releaseName;
    }

    public void setReleaseName(String releaseName) {
        this.releaseName = releaseName;
    }

    public BigInteger getConfigItemId() {
        return configItemId;
    }

    public void setConfigItemId(BigInteger configItemId) {
        this.configItemId = configItemId;
    }

    public String getOldContent() {
        return oldContent;
    }

    public void setOldContent(String oldContent) {
        this.oldContent = oldContent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ReleaseType getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(ReleaseType releaseType) {
        this.releaseType = releaseType;
    }
}
