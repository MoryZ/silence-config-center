package com.old.silence.config.center.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.old.silence.config.center.domain.model.ConfigEnvironment;

import java.math.BigInteger;
import java.util.List;

public interface ConfigEnvironmentRepository {

    List<ConfigEnvironment> query(QueryWrapper<ConfigEnvironment> queryWrapper);
    
    /**
     * 创建配置环境
     * @param configEnvironment 配置环境信息
     */
    int create(ConfigEnvironment configEnvironment);
    
    /**
     * 更新配置环境
     * @param configEnvironment 配置环境信息
     */
    int update(ConfigEnvironment configEnvironment);
    
    /**
     * 删除配置环境
     * @param id 配置环境ID
     */
    int deleteById(BigInteger id);
    
    
} 