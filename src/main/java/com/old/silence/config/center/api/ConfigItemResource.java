package com.old.silence.config.center.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.old.silence.core.enums.EnumValueFactory;
import com.old.silence.config.center.api.assembler.ConfigItemMapper;
import com.old.silence.config.center.domain.model.ConfigItem;
import com.old.silence.config.center.domain.repository.ConfigItemRepository;
import com.old.silence.config.center.domain.service.LongPollingService;
import com.old.silence.config.center.dto.ConfigItemCommand;
import com.old.silence.config.center.dto.ConfigItemContentCommand;
import com.old.silence.config.center.enums.ConfigItemFormatType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ConfigItemResource {
    
    private final ConfigItemRepository configItemRepository;
    private final ConfigItemMapper configItemMapper;
    private final LongPollingService longPollingService;

    public ConfigItemResource(ConfigItemRepository configItemRepository, ConfigItemMapper configItemMapper,
                              LongPollingService longPollingService) {
        this.configItemRepository = configItemRepository;
        this.configItemMapper = configItemMapper;
        this.longPollingService = longPollingService;
    }

    @RequestMapping(value = "/configItems",params  = {"!pageNo", "!pageSize", "namespace", "env", "componentId", "type"})
    public ResponseEntity<String> queryConfigItem(
            @RequestParam String namespace,
            @RequestParam String env,
            @RequestParam String componentId,
            @RequestParam String type){
        var formatType = EnumValueFactory.getRequired(ConfigItemFormatType.class, type.toUpperCase());
        return new ResponseEntity<>(configItemRepository.findByCriteria(namespace, env, componentId, formatType), HttpStatus.OK);
    }

    @GetMapping(value = "/configItems", params = {"pageNo", "pageSize"})
    public Page<ConfigItem> query(Page<ConfigItem> page,
                                             @RequestParam BigInteger configEnvironmentId) {
        return configItemRepository.query(page, configEnvironmentId);
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
        configItemRepository.update(configItem);
    }

    @PutMapping("/configItems/{id}/content")
    public int updateConfigContent(@PathVariable BigInteger id, @RequestBody ConfigItemContentCommand command) {
        return configItemRepository.updateContentById(command.getContent(), id);
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