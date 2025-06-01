package com.old.silence.config.center.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.old.silence.config.center.domain.model.ConfigItemReleaseHistory;

import java.math.BigInteger;

public interface ConfigItemReleaseHistoryRepository {

    /**
     * 获取配置发布项详情
     * @param id 配置发布项ID
     * @return 配置发布项详情
     */
    ConfigItemReleaseHistory findById(BigInteger id);
    
    /**
     * 获取配置发布项列表
     * @param page 分页信息
     * @param queryWrapper 查询条件
     * @return 配置发布项列表
     */
    Page<ConfigItemReleaseHistory> query(Page<ConfigItemReleaseHistory> page, QueryWrapper<ConfigItemReleaseHistory> queryWrapper);
    
    /**
     * 创建配置发布项
     * @param configItemReleaseHistory 配置发布项信息
     */
    void create(ConfigItemReleaseHistory configItemReleaseHistory);

    /**
     * 发布 配置项
     * @param configItemReleaseHistory 配置发布项信息
     */
    void release(ConfigItemReleaseHistory configItemReleaseHistory);

    /**
     * 更新配置发布项
     * @param configItemReleaseHistory 配置发布项信息
     */
    void update(ConfigItemReleaseHistory configItemReleaseHistory);
    

} 