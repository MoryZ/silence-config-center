package com.old.silence.config.center.domain.repository;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.old.silence.config.center.domain.model.ConfigSubsystem;

import java.math.BigInteger;
import java.util.List;

public interface ConfigSubsystemRepository {

    /**
     * 获取子系统列表
     */
    List<ConfigSubsystem> query(QueryWrapper<ConfigSubsystem> queryWrapper);

    /**
     * 分页获取子系统列表
     */
    Page<ConfigSubsystem> query(Page<ConfigSubsystem> page, QueryWrapper<ConfigSubsystem> queryWrapper);

    /**
     * 获取子系统详情
     */
    ConfigSubsystem findById(BigInteger id);
    /**
     * 创建子系统
     */
    int create(ConfigSubsystem configSubsystem);
    
    /**
     * 更新子系统
     */
    int update(ConfigSubsystem configSubsystem);
    
    /**
     * 删除子系统
     */
    int deleteById(BigInteger id);
    


} 