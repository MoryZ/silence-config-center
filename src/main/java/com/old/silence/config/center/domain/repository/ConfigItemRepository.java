package com.old.silence.config.center.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.old.silence.config.center.domain.model.ConfigItem;
import com.old.silence.config.center.enums.ConfigItemFormatType;

import java.math.BigInteger;
import java.util.List;

public interface ConfigItemRepository {

    /**
     * 获取配置项详情
     * @param id 配置项ID
     * @return 配置项详情
     */
    ConfigItem findById(BigInteger id);

    /**
     * 获取配置项列表
     * @param page 页码和分页大小
     * @param configEnvironmentId 配置环境id
     * @return 配置项列表
     */
    Page<ConfigItem> query(Page<ConfigItem> page, BigInteger configEnvironmentId);

    /**
     * 创建配置项
     * @param configItem 配置项信息
     */
    int create(ConfigItem configItem);

    /**
     * 更新配置项
     * @param configItem 配置项信息
     */
    int update(ConfigItem configItem);

    /**
     * 更新配置项
     * @param content 新配置项信息
     * @param id 配置项主键
     */
    int updateContentById(String content, BigInteger id);

    /**
     * 删除配置项
     * @param id 配置项ID
     */
    int deleteById(BigInteger id);
    
    /**
     * 批量删除配置项
     * @param ids 配置项ID列表
     */
    void batchDeleteConfig(List<BigInteger> ids);
    
    /**
     * 查询配置项
     * @param namespace 命名空间
     * @param env 环境
     * @param componentId 应用id
     * @param type 配置类型
     * @return 查询配置
     */
    String findByNameSpaceIdAndEnvNameAndComponentCodeAndFormatType(String namespace, String env, String componentId, ConfigItemFormatType type);
}