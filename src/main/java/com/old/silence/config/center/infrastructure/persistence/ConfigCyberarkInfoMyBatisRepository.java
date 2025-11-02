package com.old.silence.config.center.infrastructure.persistence;

import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.old.silence.config.center.domain.model.ConfigAccessKeys;
import com.old.silence.config.center.domain.model.ConfigCyberarkInfo;
import com.old.silence.config.center.domain.repository.ConfigCyberarkInfoRepository;
import com.old.silence.config.center.infrastructure.persistence.dao.ConfigCyberarkInfoDao;

import java.math.BigInteger;

/**
 * @author moryzang
 */
@Repository
public class ConfigCyberarkInfoMyBatisRepository implements ConfigCyberarkInfoRepository {

    private final ConfigCyberarkInfoDao configCyberarkInfoDao;

    public ConfigCyberarkInfoMyBatisRepository(ConfigCyberarkInfoDao configCyberarkInfoDao) {
        this.configCyberarkInfoDao = configCyberarkInfoDao;
    }

    @Override
    public ConfigCyberarkInfo findByComponentCodeAndCyberarkObject(String componentCode, String cyberarkObject) {
        return configCyberarkInfoDao.findByComponentCodeAndCyberarkObject(componentCode, cyberarkObject, Boolean.TRUE);
    }

    @Override
    public Page<ConfigCyberarkInfo> query(Page<ConfigCyberarkInfo> page, QueryWrapper<ConfigCyberarkInfo> queryWrapper) {
        return configCyberarkInfoDao.selectPage(page, queryWrapper);
    }

    @Override
    public int create(ConfigCyberarkInfo configCyberarkInfo) {
        return configCyberarkInfoDao.insert(configCyberarkInfo);
    }

    @Override
    public int update(ConfigCyberarkInfo configCyberarkInfo) {
        return configCyberarkInfoDao.updateById(configCyberarkInfo);
    }

    @Override
    public int updateEnabled(boolean enabled, BigInteger id) {
        UpdateWrapper<ConfigCyberarkInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(ConfigCyberarkInfo::getId, id).set(ConfigCyberarkInfo::getEnabled, enabled);
        return configCyberarkInfoDao.update(updateWrapper);
    }

    @Override
    public int deleteById(BigInteger id) {
        return configCyberarkInfoDao.deleteById(id);
    }
}
