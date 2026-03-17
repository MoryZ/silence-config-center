package com.old.silence.config.center.infrastructure.persistence;

import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.old.silence.config.center.domain.model.ConfigItemHistory;
import com.old.silence.config.center.domain.repository.ConfigItemHistoryRepository;
import com.old.silence.config.center.infrastructure.persistence.dao.ConfigItemHistoryDao;

import java.math.BigInteger;
import java.util.List;

/**
 * @author moryzang
 */
@Repository
public class ConfigItemHistoryMyBatisRepository implements ConfigItemHistoryRepository {

    private final ConfigItemHistoryDao configItemHistoryDao;

    public ConfigItemHistoryMyBatisRepository(ConfigItemHistoryDao configItemHistoryDao) {
        this.configItemHistoryDao = configItemHistoryDao;
    }

    @Override
    public void create(ConfigItemHistory configItemHistory) {
        configItemHistoryDao.insert(configItemHistory);
    }

    @Override
    public void update(ConfigItemHistory configItemHistory) {
        configItemHistoryDao.updateById(configItemHistory);
    }

    @Override
    public List<ConfigItemHistory> findByConfigItemId(BigInteger configItemId) {
        QueryWrapper<ConfigItemHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ConfigItemHistory::getConfigItemId, configItemId);
        return configItemHistoryDao.selectList(queryWrapper);
    }

    @Override
    public ConfigItemHistory findById(BigInteger id) {
        return configItemHistoryDao.selectById(id);
    }
}
