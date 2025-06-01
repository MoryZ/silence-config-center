package com.old.silence.config.center.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.old.silence.config.center.domain.model.ConfigItemHistory;

public interface ConfigHistoryRepository {
    
    /**
     * 记录配置项变更历史
     */
    void recordHistory(ConfigItemHistory history);
    
    /**
     * 获取配置项的变更历史列表
     */
    Page<ConfigItemHistory> getHistoryList(Long configId, int page, int size);
    
    /**
     * 获取配置项的最近一次变更记录
     */
    ConfigItemHistory getLatestHistory(Long configId);
    
    /**
     * 比较两个历史版本的差异
     */
    String compareVersions(Long historyId1, Long historyId2);
    
    /**
     * 回滚到指定的历史版本
     */
    void rollbackToVersion(Long historyId);
} 