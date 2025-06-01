package com.old.silence.config.center.infrastructure.persistence;

import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.old.silence.config.center.domain.model.ConfigSubsystem;
import com.old.silence.config.center.domain.repository.ConfigSubsystemRepository;
import com.old.silence.config.center.infrastructure.persistence.dao.ConfigSubsystemDao;

import java.math.BigInteger;
import java.util.List;

/**
 * @author moryzang
 */
@Repository
public class ConfigSubsystemMyBatisRepository implements ConfigSubsystemRepository {

    private final ConfigSubsystemDao configSubsystemDao;

    public ConfigSubsystemMyBatisRepository(ConfigSubsystemDao configSubsystemDao) {
        this.configSubsystemDao = configSubsystemDao;
    }

    @Override
    public List<ConfigSubsystem> query(QueryWrapper<ConfigSubsystem> queryWrapper) {
        return configSubsystemDao.selectList(queryWrapper);
    }

    @Override
    public Page<ConfigSubsystem> query(Page<ConfigSubsystem> page, QueryWrapper<ConfigSubsystem> queryWrapper) {
        return configSubsystemDao.selectPage(page, queryWrapper);
    }

    @Override
    public ConfigSubsystem findById(BigInteger id) {
        return configSubsystemDao.selectById(id);
    }

    @Override
    public int create(ConfigSubsystem configSubsystem) {
        return configSubsystemDao.insert(configSubsystem);
    }

    @Override
    public int update(ConfigSubsystem configSubsystem) {
        return configSubsystemDao.updateById(configSubsystem);
    }

    @Override
    public int deleteById(BigInteger id) {
        return configSubsystemDao.deleteById(id);
    }

}
