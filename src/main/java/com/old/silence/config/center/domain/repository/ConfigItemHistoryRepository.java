package com.old.silence.config.center.domain.repository;

import com.old.silence.config.center.domain.model.ConfigItemHistory;

import java.math.BigInteger;
import java.util.List;

public interface ConfigItemHistoryRepository {
    
    /**
     * 创建配置项
     * @param configItemHistory 配置项信息
     */
    void create(ConfigItemHistory configItemHistory);
    
    /**
     * 更新配置项
     * @param configItemHistory 配置项信息
     */
    void update(ConfigItemHistory configItemHistory);
    
    /**
     * 获取配置项列表
     * @param configItemId 配置环境id
     * @return 配置项列表
     */
    List<ConfigItemHistory> findByConfigItemId(BigInteger configItemId);
    
    /**
     * 获取配置项详情
     * @param id 配置项ID
     * @return 配置项详情
     */
    ConfigItemHistory findById(BigInteger id);
    

} 