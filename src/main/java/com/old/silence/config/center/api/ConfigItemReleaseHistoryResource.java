package com.old.silence.config.center.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.old.silence.config.center.api.config.annotation.SignatureAuth;
import com.old.silence.data.commons.converter.QueryWrapperConverter;
import com.old.silence.config.center.api.assembler.ConfigItemReleaseHistoryMapper;
import com.old.silence.config.center.domain.model.ConfigItemReleaseHistory;
import com.old.silence.config.center.domain.repository.ConfigItemReleaseHistoryRepository;
import com.old.silence.config.center.dto.ConfigItemReleaseHistoryCommand;
import com.old.silence.config.center.dto.ConfigItemReleaseHistoryQuery;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/v1")
public class ConfigItemReleaseHistoryResource {

    private final ConfigItemReleaseHistoryRepository configItemReleaseHistoryRepository;
    private final ConfigItemReleaseHistoryMapper configItemReleaseHistoryMapper;

    public ConfigItemReleaseHistoryResource(ConfigItemReleaseHistoryRepository configItemReleaseHistoryRepository,
                                            ConfigItemReleaseHistoryMapper configItemReleaseHistoryMapper) {
        this.configItemReleaseHistoryRepository = configItemReleaseHistoryRepository;
        this.configItemReleaseHistoryMapper = configItemReleaseHistoryMapper;
    }

    @GetMapping(value = "/configItemReleaseHistories", params = {"pageNo", "pageSize"})
    public Page<ConfigItemReleaseHistory> query(Page<ConfigItemReleaseHistory> page, ConfigItemReleaseHistoryQuery query) {
        var queryWrapper = QueryWrapperConverter.convert(query, ConfigItemReleaseHistory.class);
        return configItemReleaseHistoryRepository.query(page, queryWrapper);
    }

    @GetMapping("/configItemReleaseHistories/{id}")
    public ConfigItemReleaseHistory findById(@PathVariable BigInteger id) {
        return configItemReleaseHistoryRepository.findById(id);
    }

    @PostMapping("/configItemReleaseHistories")
    public void create(@RequestBody ConfigItemReleaseHistoryCommand configItemReleaseHistoryCommand) {
        var configItemReleaseHistory = configItemReleaseHistoryMapper.convert(configItemReleaseHistoryCommand);
        configItemReleaseHistoryRepository.create(configItemReleaseHistory);
    }

    @SignatureAuth
    @PutMapping("/configItemReleaseHistories/release")
    public void release(@RequestBody ConfigItemReleaseHistoryCommand configItemReleaseHistoryCommand) {
        var configItemReleaseHistory = configItemReleaseHistoryMapper.convert(configItemReleaseHistoryCommand);
        configItemReleaseHistoryRepository.release(configItemReleaseHistory);
    }
}