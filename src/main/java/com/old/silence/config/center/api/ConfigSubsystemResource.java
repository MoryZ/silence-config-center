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
import com.old.silence.data.commons.converter.QueryWrapperConverter;
import com.old.silence.config.center.api.assembler.ConfigSubsystemMapper;
import com.old.silence.config.center.domain.model.ConfigSubsystem;
import com.old.silence.config.center.domain.repository.ConfigSubsystemRepository;
import com.old.silence.config.center.dto.ConfigSubsystemCommand;
import com.old.silence.config.center.dto.ConfigSubsystemQuery;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ConfigSubsystemResource {

    private final ConfigSubsystemRepository configSubsystemRepository;
    private final ConfigSubsystemMapper configSubsystemMapper;

    public ConfigSubsystemResource(ConfigSubsystemRepository configSubsystemRepository,
                                   ConfigSubsystemMapper configSubsystemMapper) {
        this.configSubsystemRepository = configSubsystemRepository;
        this.configSubsystemMapper = configSubsystemMapper;
    }

    @GetMapping(value = "/configSubsystems",params  = {"pageNo", "pageSize"})
    public Page<ConfigSubsystem> queryConfigItem(Page<ConfigSubsystem> page, ConfigSubsystemQuery configSubsystemQuery){
        var queryWrapper = QueryWrapperConverter.convert(configSubsystemQuery, ConfigSubsystem.class);
        return configSubsystemRepository.query(page, queryWrapper);
    }

    @GetMapping(value = "/configSubsystems",params  = {"!pageNo", "!pageSize"})
    public List<ConfigSubsystem> queryConfigItem(ConfigSubsystemQuery configSubsystemQuery){
        var queryWrapper = QueryWrapperConverter.convert(configSubsystemQuery, ConfigSubsystem.class);
        return configSubsystemRepository.query(queryWrapper);
    }

    @PostMapping("/configSubsystems")
    public void create(@RequestBody ConfigSubsystemCommand configSubsystemCommand) {
        var configSubsystem = configSubsystemMapper.convert(configSubsystemCommand);
        configSubsystemRepository.create(configSubsystem);
    }

    @PutMapping("/configSubsystems/{id}")
    public void update(@PathVariable BigInteger id, @RequestBody ConfigSubsystemCommand configSubsystemCommand) {
        var configSubsystem = configSubsystemMapper.convert(configSubsystemCommand);
        configSubsystem.setId(id);
        configSubsystemRepository.update(configSubsystem);
    }

    @DeleteMapping("/configSubsystems/{id}")
    public void delete(@PathVariable BigInteger id) {
        configSubsystemRepository.deleteById(id);
    }


}