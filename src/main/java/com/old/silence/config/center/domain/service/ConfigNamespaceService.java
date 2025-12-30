package com.old.silence.config.center.domain.service;

import org.springframework.stereotype.Service;
import com.old.silence.config.center.domain.model.ConfigItem;
import com.old.silence.config.center.domain.repository.ConfigItemRepository;
import com.old.silence.config.center.enums.CloneMode;
import com.old.silence.config.center.enums.NameSpaceStatus;
import com.old.silence.core.util.CollectionUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author moryzang
 */
@Service
public class ConfigNamespaceService {

    private final ConfigItemRepository configItemRepository;

    public ConfigNamespaceService(ConfigItemRepository configItemRepository) {
        this.configItemRepository = configItemRepository;
    }

    public boolean clone(BigInteger sourceEnvironmentId, List<BigInteger> targetEnvironmentIds, CloneMode cloneMode) {
        var sourceConfigItems = configItemRepository.findByConfigEnvironmentId(sourceEnvironmentId);

        targetEnvironmentIds.forEach(targetEnvironmentId -> {
            // 如果是覆盖文件 ，有相同文件名的 覆盖掉？(修改)
            var targetConfigItems = configItemRepository.findByConfigEnvironmentId(targetEnvironmentId);
            if (CloneMode.OVERWRITE_FILES.equals(cloneMode)) {

                // 构建目标环境的文件名映射，便于快速查找
                Map<String, ConfigItem> targetItemMap = targetConfigItems.stream()
                        .collect(Collectors.toMap(ConfigItem::getNamespaceId, Function.identity()));

                List<ConfigItem> itemsToSave = new ArrayList<>();
                List<ConfigItem> itemsToUpdate = new ArrayList<>();

                for (ConfigItem sourceItem : sourceConfigItems) {
                    String namespaceId = sourceItem.getNamespaceId();

                    if (targetItemMap.containsKey(namespaceId)) {
                        // 同名文件：更新目标环境的配置项
                        ConfigItem targetItem = targetItemMap.get(namespaceId);
                        targetItem.setOldContent(targetItem.getContent());
                        targetItem.setContent(sourceItem.getContent());
                        targetItem.setMd5(sourceItem.getMd5());
                        targetItem.setType(sourceItem.getType());
                        targetItem.setFormatType(sourceItem.getFormatType());
                        targetItem.setNamespaceStatus(NameSpaceStatus.SAVED);
                        targetItem.setConfigEnvironmentId(targetEnvironmentId);

                        // 这种是更新的
                        itemsToUpdate.add(targetItem);


                    } else {
                        // 新文件：创建新的配置项
                        ConfigItem newItem = createNewConfigItem(sourceItem, targetEnvironmentId);
                        // 这种是新增的
                        itemsToSave.add(newItem);
                    }
                }

                // 批量保存
                if (CollectionUtils.isNotEmpty(itemsToSave)) {
                    configItemRepository.bulkCreate(itemsToSave);
                }

                if (CollectionUtils.isNotEmpty(itemsToUpdate)) {
                    configItemRepository.bulkUpdate(itemsToUpdate);
                }

            } else {
                // 如果是跳过同名文件 ，只新增
                // 获取目标环境已有的文件名
                Set<String> existingNamespaces = targetConfigItems.stream()
                        .map(ConfigItem::getNamespaceId)
                        .collect(Collectors.toSet());

                // 过滤出源环境中不存在于目标环境的新文件
                List<ConfigItem> newItems = sourceConfigItems.stream()
                        .filter(sourceItem -> !existingNamespaces.contains(sourceItem.getNamespaceId()))
                        .map(sourceItem -> createNewConfigItem(sourceItem, targetEnvironmentId))
                        .collect(Collectors.toList());

                // 保存新文件
                configItemRepository.bulkCreate(newItems);
            }
        });

        return true;
    }

    // 创建新的配置项对象（复制源配置项但使用目标环境ID）
    private ConfigItem createNewConfigItem(ConfigItem sourceItem, BigInteger targetEnvironmentId) {
        ConfigItem newItem = new ConfigItem();
        newItem.setNamespaceId(sourceItem.getNamespaceId());
        newItem.setOldContent(sourceItem.getOldContent());
        newItem.setContent(sourceItem.getContent());
        newItem.setMd5(sourceItem.getMd5());
        newItem.setType(sourceItem.getType());
        newItem.setFormatType(sourceItem.getFormatType());
        newItem.setNamespaceStatus(NameSpaceStatus.SAVED);
        newItem.setConfigEnvironmentId(targetEnvironmentId);
        return newItem;
    }
}
