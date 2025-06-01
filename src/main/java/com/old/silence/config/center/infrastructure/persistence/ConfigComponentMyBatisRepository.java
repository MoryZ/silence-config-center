package com.old.silence.config.center.infrastructure.persistence;

import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.old.silence.config.center.domain.model.ConfigComponent;
import com.old.silence.config.center.domain.repository.ConfigComponentRepository;
import com.old.silence.config.center.infrastructure.persistence.dao.ConfigComponentDao;

import java.math.BigInteger;
import java.util.List;

/**
 * @author moryzang
 */
@Repository
public class ConfigComponentMyBatisRepository implements ConfigComponentRepository {

    private final ConfigComponentDao configComponentDao;
    public ConfigComponentMyBatisRepository(ConfigComponentDao configComponentDao) {
        this.configComponentDao = configComponentDao;
    }

    @Override
    public List<ConfigComponent> findBySubsystemId(QueryWrapper<ConfigComponent> queryWrapper) {
        return configComponentDao.selectList(queryWrapper);
    }

    @Override
    public int create(ConfigComponent configComponent) {
        return configComponentDao.insert(configComponent);
    }

    @Override
    public int update(ConfigComponent configComponent) {
        return configComponentDao.updateById(configComponent);
    }

    @Override
    public int deleteById(BigInteger id) {
        return configComponentDao.deleteById(id);
    }
}
