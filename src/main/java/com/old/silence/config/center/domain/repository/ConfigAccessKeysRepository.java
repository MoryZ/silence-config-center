package com.old.silence.config.center.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.old.silence.config.center.domain.model.ConfigAccessKeys;

import java.math.BigInteger;

public interface ConfigAccessKeysRepository {

    ConfigAccessKeys findByAccessKey(String accessKey);

    Page<ConfigAccessKeys> query(Page<ConfigAccessKeys> page, QueryWrapper<ConfigAccessKeys> queryWrapper);

    /**
     * 创建访问密钥私钥
     *
     * @param configAccessKeys 密钥私钥
     */
    int create(ConfigAccessKeys configAccessKeys);

    /**
     * 更新访问密钥私钥
     *
     * @param configEnvironment 密钥私钥
     */
    int update(ConfigAccessKeys configEnvironment);

    /**
     * 更新访问密钥私钥状态
     * @param enabled 是否启用
     * @param id id
     * @return 受影响行数
     */
    int updateEnabled(boolean enabled, BigInteger id);

    /**
     * 删除访问密钥
     *
     * @param id 访问密钥的ID
     */
    int deleteById(BigInteger id);



}