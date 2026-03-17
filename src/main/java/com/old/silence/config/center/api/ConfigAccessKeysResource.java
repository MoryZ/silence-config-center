package com.old.silence.config.center.api;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.old.silence.config.center.api.assembler.ConfigAccessKeysMapper;
import com.old.silence.config.center.domain.model.ConfigAccessKeys;
import com.old.silence.config.center.domain.repository.ConfigAccessKeysRepository;
import com.old.silence.config.center.dto.ConfigAccessKeysCommand;
import com.old.silence.config.center.dto.ConfigAccessKeysQuery;
import com.old.silence.data.commons.converter.QueryWrapperConverter;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/v1")
public class ConfigAccessKeysResource {

    private final ConfigAccessKeysRepository configAccessKeysRepository;
    private final ConfigAccessKeysMapper configAccessKeysMapper;

    public ConfigAccessKeysResource(ConfigAccessKeysRepository configAccessKeysRepository,
                                    ConfigAccessKeysMapper configAccessKeysMapper) {
        this.configAccessKeysRepository = configAccessKeysRepository;
        this.configAccessKeysMapper = configAccessKeysMapper;
    }

    @GetMapping(value = "/configAccessKeys", params = {"pageNo", "pageSize" })
    public Page<ConfigAccessKeys> query(Page<ConfigAccessKeys> page, ConfigAccessKeysQuery query) {
        var queryWrapper = QueryWrapperConverter.convert(query, ConfigAccessKeys.class);
        return configAccessKeysRepository.query(page, queryWrapper);
    }

    @PostMapping("/configAccessKeys")
    public BigInteger create(@RequestBody ConfigAccessKeysCommand configAccessKeysCommand) {
        var configAccessKeys = configAccessKeysMapper.convert(configAccessKeysCommand);
        configAccessKeysRepository.create(configAccessKeys);
        return configAccessKeys.getId();
    }

    @PutMapping("/configAccessKeys/{id}")
    public void update(@PathVariable BigInteger id, @RequestBody ConfigAccessKeysCommand configAccessKeysCommand) {
        var configAccessKeys = configAccessKeysMapper.convert(configAccessKeysCommand);
        configAccessKeys.setId(id);
        configAccessKeysRepository.update(configAccessKeys);
    }

    @PutMapping("/configAccessKeys/{id}/enable")
    public void enable(@PathVariable BigInteger id) {
        configAccessKeysRepository.updateEnabled(true, id);
    }

    @PutMapping("/configAccessKeys/{id}/disable")
    public void disable(@PathVariable BigInteger id) {
        configAccessKeysRepository.updateEnabled(false, id);
    }


    @DeleteMapping("/configAccessKeys/{id}")
    public void delete(@PathVariable BigInteger id) {
        configAccessKeysRepository.deleteById(id);
    }

}