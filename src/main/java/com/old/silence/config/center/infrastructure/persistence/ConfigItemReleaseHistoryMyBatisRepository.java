package com.old.silence.config.center.infrastructure.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.old.silence.config.center.domain.model.ConfigItemReleaseHistory;
import com.old.silence.config.center.domain.repository.ConfigItemReleaseHistoryRepository;
import com.old.silence.config.center.domain.service.LongPollingService;
import com.old.silence.config.center.enums.EventType;
import com.old.silence.config.center.enums.NameSpaceStatus;
import com.old.silence.config.center.infrastructure.persistence.dao.ConfigItemDao;
import com.old.silence.config.center.infrastructure.persistence.dao.ConfigItemReleaseHistoryDao;

import java.math.BigInteger;

/**
 * @author moryzang
 */
@Repository
public class ConfigItemReleaseHistoryMyBatisRepository implements ConfigItemReleaseHistoryRepository {

    private static final Logger logger = LoggerFactory.getLogger(ConfigItemReleaseHistoryMyBatisRepository.class);
    private final ConfigItemReleaseHistoryDao configItemReleaseHistoryDao;
    private final ConfigItemDao configItemDao;
    private final LongPollingService longPollingService;

    public ConfigItemReleaseHistoryMyBatisRepository(ConfigItemReleaseHistoryDao configItemReleaseHistoryDao,
                                                     ConfigItemDao configItemDao,
                                                     LongPollingService longPollingService) {
        this.configItemReleaseHistoryDao = configItemReleaseHistoryDao;
        this.configItemDao = configItemDao;
        this.longPollingService = longPollingService;
    }



    @Override
    public void release(ConfigItemReleaseHistory configItemReleaseHistory) {
        // 发布历史记录表
        configItemReleaseHistoryDao.insert(configItemReleaseHistory);

        var configReleaseVo = configItemDao.findReleaseInfoById(configItemReleaseHistory.getConfigItemId());
        if (configReleaseVo == null) {
            logger.warn("未找到配置项的发布信息，configItemId={}", configItemReleaseHistory.getConfigItemId());
        } else {
            boolean delivered = longPollingService.notifySubscriber(
                    EventType.PUBLISH,
                    configReleaseVo.getEnv(),
                    configReleaseVo.getCode(),
                    configReleaseVo.getNamespaceId(),
                    configItemReleaseHistory.getContent());
            if (!delivered) {
                logger.info("暂无在线客户端，发布事件暂存 [{}-{}-{}]",
                        configReleaseVo.getCode(),
                        configReleaseVo.getEnv(),
                        configReleaseVo.getNamespaceId());
            }
        }

        configItemDao.updateNamespaceStatusById(NameSpaceStatus.PUBLISHED, configItemReleaseHistory.getConfigItemId());
    }
    @Override
    public ConfigItemReleaseHistory findById(BigInteger id) {
        return configItemReleaseHistoryDao.selectById(id);
    }

    @Override
    public Page<ConfigItemReleaseHistory> query(Page<ConfigItemReleaseHistory> page, QueryWrapper<ConfigItemReleaseHistory> queryWrapper) {
        return configItemReleaseHistoryDao.selectPage(page, queryWrapper);
    }

    @Override
    public void create(ConfigItemReleaseHistory configItemReleaseHistory) {
        configItemReleaseHistoryDao.insert(configItemReleaseHistory);
    }

    @Override
    public void update(ConfigItemReleaseHistory configItemReleaseHistory) {
        configItemReleaseHistoryDao.updateById(configItemReleaseHistory);
    }
}
