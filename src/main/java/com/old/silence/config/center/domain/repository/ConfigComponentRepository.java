package com.old.silence.config.center.domain.repository;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.old.silence.config.center.domain.model.ConfigComponent;

import java.math.BigInteger;
import java.util.List;

public interface ConfigComponentRepository {

    /**
     * 获取子系统下的所有组件
     */
    List<ConfigComponent> findBySubsystemId(QueryWrapper<ConfigComponent> queryWrapper);
    /**
     * 创建组件
     */
    int create(ConfigComponent configComponent);
    
    /**
     * 更新组件
     */
    int update(ConfigComponent configComponent);
    
    /**
     * 删除组件
     */
    int deleteById(BigInteger id);



}