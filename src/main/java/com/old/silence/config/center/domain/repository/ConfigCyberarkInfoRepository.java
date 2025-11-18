package com.old.silence.config.center.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.old.silence.config.center.domain.model.ConfigCyberarkInfo;

import java.math.BigInteger;

public interface ConfigCyberarkInfoRepository {

    /**
     * 根据组件代码和cyberark对象查询密码映射
     * @param componentCode 组件代码
     * @param cyberarkObject cyberark对象
     * @return 密码映射
     */
    ConfigCyberarkInfo findByComponentCodeAndCyberarkObject(String componentCode, String cyberarkObject);

    /**
     * 分页查询密码映射
     * @param page 分页对象
     * @param queryWrapper 查询条件
     * @return 分页结果
     */
    Page<ConfigCyberarkInfo> query(Page<ConfigCyberarkInfo> page, QueryWrapper<ConfigCyberarkInfo> queryWrapper);

    /**
     * 创建密码映射
     *
     * @param configCyberarkInfo 密码映射
     */
    int create(ConfigCyberarkInfo configCyberarkInfo);

    /**
     * 更新密码映射
     *
     * @param configCyberarkInfo 密码映射
     */
    int update(ConfigCyberarkInfo configCyberarkInfo);

    /**
     * 更新密码映射状态
     * @param enabled 是否启用
     * @param id id
     * @return 受影响行数
     */
    int updateEnabled(boolean enabled, BigInteger id);

    /**
     * 删除密码映射
     *
     * @param id cyberark 的ID
     */
    int deleteById(BigInteger id);


}