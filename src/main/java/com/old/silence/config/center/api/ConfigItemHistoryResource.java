package com.old.silence.config.center.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.old.silence.config.center.api.assembler.ConfigItemHistoryMapper;
import com.old.silence.config.center.domain.model.ConfigItemHistory;
import com.old.silence.config.center.domain.repository.ConfigItemHistoryRepository;
import com.old.silence.config.center.dto.ConfigItemHistoryCommand;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ConfigItemHistoryResource {

    private final ConfigItemHistoryRepository configItemHistoryRepository;
    private final ConfigItemHistoryMapper configItemHistoryMapper;

    public ConfigItemHistoryResource(ConfigItemHistoryRepository configItemHistoryRepository,
                                     ConfigItemHistoryMapper configItemHistoryMapper) {
        this.configItemHistoryRepository = configItemHistoryRepository;
        this.configItemHistoryMapper = configItemHistoryMapper;
    }

    @GetMapping(value = "/configItemHistories", params = {"configItemId"})
    public List<ConfigItemHistory> query(@RequestParam BigInteger configItemId) {
        return configItemHistoryRepository.findByConfigItemId(configItemId);
    }

    @GetMapping("/configItemHistories/{id}")
    public ConfigItemHistory findById(@PathVariable BigInteger id) {
        return configItemHistoryRepository.findById(id);
    }

    @PostMapping("/configItemHistories")
    public void create(@RequestBody ConfigItemHistoryCommand configItemHistoryCommand) {
        var configItemHistory = configItemHistoryMapper.convert(configItemHistoryCommand);
        configItemHistoryRepository.create(configItemHistory);
    }
}