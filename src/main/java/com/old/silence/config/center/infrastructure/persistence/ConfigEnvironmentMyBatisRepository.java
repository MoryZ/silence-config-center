package com.old.silence.config.center.infrastructure.persistence;

import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.old.silence.config.center.domain.model.ConfigEnvironment;
import com.old.silence.config.center.domain.repository.ConfigEnvironmentRepository;
import com.old.silence.config.center.infrastructure.persistence.dao.ConfigEnvironmentDao;

import java.math.BigInteger;
import java.util.List;

/**
 * @author moryzang
 */
@Repository
public class ConfigEnvironmentMyBatisRepository implements ConfigEnvironmentRepository {

    private final ConfigEnvironmentDao configEnvironmentDao;
    public ConfigEnvironmentMyBatisRepository(ConfigEnvironmentDao configEnvironmentDao) {
        this.configEnvironmentDao = configEnvironmentDao;
    }

    @Override
    public List<ConfigEnvironment> query(QueryWrapper<ConfigEnvironment> queryWrapper) {
        return configEnvironmentDao.selectList(queryWrapper);
    }

    @Override
    public int create(ConfigEnvironment configEnvironment) {
        return configEnvironmentDao.insert(configEnvironment);
    }

    @Override
    public int update(ConfigEnvironment configEnvironment) {
        configEnvironmentDao.updateById(configEnvironment);
        return 0;
    }

    @Override
    public int deleteById(BigInteger id) {
        configEnvironmentDao.deleteById(id);
        return 0;
    }
}
