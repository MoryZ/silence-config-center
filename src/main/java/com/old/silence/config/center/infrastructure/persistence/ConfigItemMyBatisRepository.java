package com.old.silence.config.center.infrastructure.persistence;

import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.old.silence.config.center.domain.model.ConfigItem;
import com.old.silence.config.center.domain.repository.ConfigItemRepository;
import com.old.silence.config.center.enums.ConfigItemFormatType;
import com.old.silence.config.center.enums.NameSpaceStatus;
import com.old.silence.config.center.infrastructure.persistence.dao.ConfigItemDao;
import com.old.silence.config.center.util.Md5Utils;

import java.math.BigInteger;
import java.util.List;

/**
 * @author moryzang
 */
@Repository
public class ConfigItemMyBatisRepository implements ConfigItemRepository {

    private final ConfigItemDao configItemDao;
    public ConfigItemMyBatisRepository(ConfigItemDao configItemDao) {
        this.configItemDao = configItemDao;
    }

    @Override
    public Page<ConfigItem> query(Page<ConfigItem> page, QueryWrapper<ConfigItem> queryWrapper) {
        return configItemDao.selectPage(page, queryWrapper);
    }

    @Override
    public ConfigItem findById(BigInteger id) {
        return configItemDao.selectById(id);
    }

    @Override
    public String findByNameSpaceIdAndEnvNameAndComponentCodeAndFormatType(String namespaceId, String env, String componentCode, ConfigItemFormatType type) {
        return configItemDao.findByNameSpaceIdAndEnvNameAndComponentCodeAndFormatType(namespaceId, env, componentCode, type);
    }


    @Override
    public int create(ConfigItem configItem) {
        configItem.setNamespaceStatus(NameSpaceStatus.SAVED);
        configItem.setOldContent(configItem.getContent());
        configItem.setMd5(Md5Utils.md5(configItem.getContent()));
        return configItemDao.insert(configItem);
    }

    @Override
    public int update(ConfigItem configItem) {
        configItem.setMd5(Md5Utils.md5(configItem.getContent()));
        return configItemDao.updateById(configItem);
    }

    @Override
    public int updateContentById(String content, BigInteger id) {

        String oldContent = findById(id).getContent();
        UpdateWrapper<ConfigItem> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id)
                .set("old_content", oldContent)
                .set("content", content)
                .set("md5", Md5Utils.md5(content));
        return configItemDao.update(null, updateWrapper);
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
