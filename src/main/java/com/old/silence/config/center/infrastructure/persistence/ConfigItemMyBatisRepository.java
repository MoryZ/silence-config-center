package com.old.silence.config.center.infrastructure.persistence;

import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.old.silence.config.center.domain.model.ConfigItem;
import com.old.silence.config.center.domain.model.ConfigItemHistory;
import com.old.silence.config.center.domain.repository.ConfigItemHistoryRepository;
import com.old.silence.config.center.domain.repository.ConfigItemRepository;
import com.old.silence.config.center.enums.ConfigItemFormatType;
import com.old.silence.config.center.enums.NameSpaceStatus;
import com.old.silence.config.center.enums.OperationType;
import com.old.silence.config.center.infrastructure.persistence.dao.ConfigItemDao;
import com.old.silence.config.center.util.Md5Utils;
import com.old.silence.core.context.CommonErrors;

import java.math.BigInteger;
import java.util.List;

/**
 * @author moryzang
 */
@Repository
public class ConfigItemMyBatisRepository implements ConfigItemRepository {

    private final ConfigItemDao configItemDao;
    private final ConfigItemHistoryRepository configItemHistoryRepository;
    public ConfigItemMyBatisRepository(ConfigItemDao configItemDao, ConfigItemHistoryRepository configItemHistoryRepository1) {
        this.configItemDao = configItemDao;
        this.configItemHistoryRepository = configItemHistoryRepository1;
    }

    @Override
    public Page<ConfigItem> queryPage(Page<ConfigItem> page, QueryWrapper<ConfigItem> queryWrapper) {
        return configItemDao.selectPage(page, queryWrapper);
    }

    @Override
    public ConfigItem findById(BigInteger id) {
        return configItemDao.selectById(id);
    }

    @Override
    public ConfigItem findByConfigEnvironmentIdAndNamespaceId(BigInteger configEnvironmentId, String namespaceId) {
        var lambdaQueryWrapper = new LambdaQueryWrapper<ConfigItem>();
        lambdaQueryWrapper.eq(ConfigItem::getConfigEnvironmentId, configEnvironmentId);
        lambdaQueryWrapper.eq(ConfigItem::getNamespaceId, namespaceId);
        return configItemDao.selectOne(lambdaQueryWrapper);
    }

    @Override
    public List<ConfigItem> findByConfigEnvironmentId(BigInteger configEnvironmentId) {
        var lambdaQueryWrapper = new LambdaQueryWrapper<ConfigItem>();
        lambdaQueryWrapper.eq(ConfigItem::getConfigEnvironmentId, configEnvironmentId);
        return configItemDao.selectList(lambdaQueryWrapper);
    }

    @Override
    public String findByNameSpaceIdAndEnvNameAndComponentCodeAndFormatType(String namespaceId, String env, String componentCode, ConfigItemFormatType type) {
        return configItemDao.findByNameSpaceIdAndEnvNameAndComponentCodeAndFormatType(namespaceId, env, componentCode, type, NameSpaceStatus.PUBLISHED);
    }


    @Override
    public int create(ConfigItem configItem) {
        configItem.setNamespaceStatus(NameSpaceStatus.SAVED);
        configItem.setOldContent(configItem.getContent());
        configItem.setMd5(Md5Utils.md5(configItem.getContent()));
        return configItemDao.insert(configItem);
    }

    @Override
    public int bulkCreate(List<ConfigItem> configItems) {
        return configItemDao.insertAll(configItems);
    }

    @Override
    public int update(ConfigItem configItem, OperationType operationType) {
        configItem.setMd5(Md5Utils.md5(configItem.getContent()));
        var rowsAffected = configItemDao.updateById(configItem);

        var configItemHistory = new ConfigItemHistory();
        configItemHistory.setConfigItemId(configItem.getId());
        configItemHistory.setContent(configItem.getContent());
        configItemHistory.setOldContent(configItem.getOldContent());

        configItemHistory.setOperationType(operationType);
        configItemHistoryRepository.create(configItemHistory);
        return rowsAffected;
    }

    @Override
    public int bulkUpdate(List<ConfigItem> configItems) {
        return configItemDao.updateAll(configItems);
    }

    @Override
    public int updateContentById(String content, OperationType operationType, BigInteger id) {
        var configItem = findById(id);
        if (configItem == null) {
            throw CommonErrors.DATA_NOT_EXIST.createException("configItemId=" + id);
        }
        String oldContent = configItem.getContent();
        UpdateWrapper<ConfigItem> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(ConfigItem::getId,id)
                .set(ConfigItem::getOldContent, oldContent)
                .set(ConfigItem::getContent, content)
                .set(ConfigItem::getNamespaceStatus, NameSpaceStatus.SAVED)
                .set(ConfigItem::getMd5, Md5Utils.md5(content));

        var configItemHistory = new ConfigItemHistory();
        configItemHistory.setConfigItemId(id);
        configItemHistory.setContent(content);
        configItemHistory.setOldContent(oldContent);
        configItemHistory.setOperationType(operationType);
        configItemHistoryRepository.create(configItemHistory);

        return configItemDao.update(updateWrapper);
    }

    @Override
    public int deleteById(BigInteger id) {
        return configItemDao.deleteById(id);
    }

    @Override
    public void batchDeleteConfig(List<BigInteger> ids) {
        configItemDao.deleteBatchIds(ids);
    }

}
