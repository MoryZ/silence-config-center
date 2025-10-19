package com.old.silence.config.center.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.old.silence.config.center.api.assembler.ConfigItemMapper;
import com.old.silence.config.center.domain.model.ConfigItem;
import com.old.silence.config.center.domain.repository.ConfigEnvironmentRepository;
import com.old.silence.config.center.domain.repository.ConfigItemRepository;
import com.old.silence.config.center.domain.service.ClientRegistryService;
import com.old.silence.config.center.domain.service.LongPollingService;
import com.old.silence.config.center.dto.ConfigItemCommand;
import com.old.silence.config.center.dto.ConfigItemContentCommand;
import com.old.silence.config.center.dto.ConfigItemQuery;
import com.old.silence.config.center.enums.ConfigItemFormatType;
import com.old.silence.config.center.vo.ConfigEnvironmentVo;
import com.old.silence.config.center.vo.ConfigItemVo;
import com.old.silence.core.util.CollectionUtils;
import com.old.silence.data.commons.converter.QueryWrapperConverter;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/v1")
public class ConfigItemResource {

    private final ConfigItemRepository configItemRepository;
    private final ConfigEnvironmentRepository configEnvironmentRepository;
    private final ConfigItemMapper configItemMapper;
    private final LongPollingService longPollingService;
    private final ClientRegistryService clientRegistryService;

    public ConfigItemResource(ConfigItemRepository configItemRepository, ConfigEnvironmentRepository configEnvironmentRepository,
                              ConfigItemMapper configItemMapper, LongPollingService longPollingService,
                              ClientRegistryService clientRegistryService) {
        this.configItemRepository = configItemRepository;
        this.configEnvironmentRepository = configEnvironmentRepository;
        this.configItemMapper = configItemMapper;
        this.longPollingService = longPollingService;
        this.clientRegistryService = clientRegistryService;
    }

    @RequestMapping(value = "/configItems", params = {"!pageNo", "!pageSize", "namespace", "env", "componentCode", "type"})
    public String queryConfigItem(
            @RequestParam String namespace,
            @RequestParam String env,
            @RequestParam String componentCode,
            @RequestParam String type) {
        var formatType = ConfigItemFormatType.valueOf(type.toUpperCase());
        return configItemRepository.findByNameSpaceIdAndEnvNameAndComponentCodeAndFormatType(namespace, env, componentCode, formatType);
    }

    @GetMapping(value = "/configItems", params = {"pageNo", "pageSize"})
    public IPage<ConfigItemVo> query(Page<ConfigItem> page, ConfigItemQuery configItemQuery) {

        var queryWrapper = QueryWrapperConverter.convert(configItemQuery, ConfigItem.class);
        var configItemPage = configItemRepository.query(page, queryWrapper);
        AtomicReference<ConfigEnvironmentVo> configEnvironmentVo = new AtomicReference<>();
        if (CollectionUtils.isNotEmpty(configItemPage.getRecords())) {
            CollectionUtils.firstElement(configItemPage.getRecords())
                    .map(ConfigItem::getConfigEnvironmentId)
                    .ifPresent(configEnvironmentId -> {
                        configEnvironmentVo.set(configEnvironmentRepository.findById(configEnvironmentId));
                    });

        }

        return configItemPage.convert(configItem -> {
            if (configEnvironmentVo.get() != null) {
                var configKey = String.format("%s+%s+%s",
                        configEnvironmentVo.get().getComponentCode(),
                        configEnvironmentVo.get().getName(),
                        configItem.getNamespaceId());
                var listeningClientIps = clientRegistryService.getListeningClientIps(configKey);
                return new ConfigItemVo(configItem, listeningClientIps);
            } else {
                return new ConfigItemVo(configItem, null);
            }

        });
    }

    @GetMapping("/configItems/{id}")
    public ConfigItem findById(@PathVariable BigInteger id) {
        return configItemRepository.findById(id);
    }

    @PostMapping("/configItems")
    public void create(@RequestBody ConfigItemCommand configItemCommand) {
        var configItem = configItemMapper.convert(configItemCommand);
        configItemRepository.create(configItem);
    }

    @PutMapping("/configItems/{id}")
    public void update(@PathVariable BigInteger id, @RequestBody ConfigItemCommand configItemCommand) {
        var configItem = configItemMapper.convert(configItemCommand);
        configItem.setId(id);
        configItemRepository.update(configItem, configItemCommand.getOperationType());
    }

    @PutMapping("/configItems/{id}/content")
    public int updateConfigContent(@PathVariable BigInteger id, @RequestBody ConfigItemContentCommand command) {
        return configItemRepository.updateContentById(command.getContent(), command.getOperationType(), id);
    }

    @GetMapping("/configItems/subscribe")
    public void subscribe(String env, String componentCode, String namespace, HttpServletRequest request, HttpServletResponse response) {
        longPollingService.subscribeConfig(env, componentCode, namespace, request, response);
    }

    @DeleteMapping("/configItems/{id}")
    public void delete(@PathVariable BigInteger id) {
        configItemRepository.deleteById(id);
    }

    @DeleteMapping("/configItems/batch")
    public void batchDeleteConfig(@RequestBody List<BigInteger> ids) {
        configItemRepository.batchDeleteConfig(ids);
    }

} 