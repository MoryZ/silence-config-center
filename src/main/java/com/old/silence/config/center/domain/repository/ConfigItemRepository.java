package com.old.silence.config.center.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.old.silence.config.center.domain.model.ConfigItem;
import com.old.silence.config.center.enums.ConfigItemFormatType;
import com.old.silence.config.center.enums.OperationType;

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
     * @param configEnvironmentId 配置环境id
     * @return 配置项列表
     */
    List<ConfigItem> findByConfigEnvironmentId(BigInteger configEnvironmentId);

    /**
     * 查询配置项
     * @param namespace 命名空间
     * @param env 环境
     * @param componentId 应用id
     * @param type 配置类型
     * @return 查询配置
     */
    String findByNameSpaceIdAndEnvNameAndComponentCodeAndFormatType(String namespace, String env, String componentId, ConfigItemFormatType type);


    /**
     * 获取配置项列表
     * @param page 页码和分页大小
     * @param queryWrapper 查询条件
     * @return 配置项列表
     */
    Page<ConfigItem> queryPage(Page<ConfigItem> page, QueryWrapper<ConfigItem> queryWrapper);

    /**
     * 创建配置项
     * @param configItem 配置项信息
     */
    int create(ConfigItem configItem);

    /**
     * 批量创建配置项
     * @param configItems 配置项信息列表
     * @return 影响条数
     */
    int bulkCreate(List<ConfigItem> configItems);

    /**
     * 批量更新配置项
     * @param configItems 配置项列表
     * @return 影响条数
     */
    int bulkUpdate(List<ConfigItem> configItems);


    /**
     * 更新配置项
     * @param configItem 配置项信息
     * @param operationType 操作类型
     */
    int update(ConfigItem configItem, OperationType operationType);

    /**
     * 更新配置项
     * @param content 新配置项信息
     * @param operationType 操作类型
     * @param id 配置项主键
     */
    int updateContentById(String content, OperationType operationType, BigInteger id);

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
    


}