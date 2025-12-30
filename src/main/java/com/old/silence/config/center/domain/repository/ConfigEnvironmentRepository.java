package com.old.silence.config.center.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.old.silence.config.center.domain.model.ConfigEnvironment;
import com.old.silence.config.center.vo.ConfigEnvironmentVo;

import java.math.BigInteger;
import java.util.List;

public interface ConfigEnvironmentRepository {

    /**
     * 查询 配置环境列表
     * @param queryWrapper 查询条件
     * @return 配置环境列表
     */
    List<ConfigEnvironment> query(QueryWrapper<ConfigEnvironment> queryWrapper);

    /**
     * 根据组件id 和环境信息 查询配置环境
     * @param configComponentId 配置组件id
     * @param environmentName 环境名称
     * @return 单个配置环境
     */
    ConfigEnvironment findByConfigComponentIdAndName(BigInteger configComponentId, String environmentName);

    /**
     * 根据id 查询
     * @param id 主键id
     * @return 配置VO
     */
    ConfigEnvironmentVo findById(BigInteger id);

    /**
     * 创建配置环境
     *
     * @param configEnvironment 配置环境信息
     */
    int create(ConfigEnvironment configEnvironment);

    /**
     * 更新配置环境
     *
     * @param configEnvironment 配置环境信息
     */
    int update(ConfigEnvironment configEnvironment);

    /**
     * 删除配置环境
     *
     * @param id 配置环境ID
     */
    int deleteById(BigInteger id);



}