package com.old.silence.config.center.infrastructure.persistence;

import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.old.silence.config.center.domain.model.ConfigAccessKeys;
import com.old.silence.config.center.domain.repository.ConfigAccessKeysRepository;
import com.old.silence.config.center.infrastructure.persistence.dao.ConfigAccessKeysDao;

import java.math.BigInteger;

/**
 * @author moryzang
 */
@Repository
public class ConfigAccessKeysMyBatisRepository implements ConfigAccessKeysRepository {

    private final ConfigAccessKeysDao configAccessKeysDao;

    public ConfigAccessKeysMyBatisRepository(ConfigAccessKeysDao configAccessKeysDao) {
        this.configAccessKeysDao = configAccessKeysDao;
    }

    @Override
    public ConfigAccessKeys findByAccessKey(String accessKey) {
        return configAccessKeysDao.findByAccessKey(accessKey, Boolean.TRUE);
    }


    @Override
    public Page<ConfigAccessKeys> query(Page<ConfigAccessKeys> page, QueryWrapper<ConfigAccessKeys> queryWrapper) {
        return configAccessKeysDao.selectPage(page, queryWrapper);
    }

    @Override
    public int create(ConfigAccessKeys configAccessKeys) {
        return configAccessKeysDao.insert(configAccessKeys);
    }

    @Override
    public int update(ConfigAccessKeys configAccessKeys) {
        return configAccessKeysDao.updateById(configAccessKeys);
    }

    @Override
    public int updateEnabled(boolean enabled, BigInteger id) {
        UpdateWrapper<ConfigAccessKeys> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(ConfigAccessKeys::getId, id).set(ConfigAccessKeys::getEnabled, enabled);
        return configAccessKeysDao.update(updateWrapper);
    }

    @Override
    public int deleteById(BigInteger id) {
        return configAccessKeysDao.deleteById(id);
    }
}
