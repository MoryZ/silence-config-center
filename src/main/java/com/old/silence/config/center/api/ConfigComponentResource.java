package com.old.silence.config.center.api;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.old.silence.data.commons.converter.QueryWrapperConverter;
import com.old.silence.config.center.api.assembler.ConfigComponentMapper;
import com.old.silence.config.center.domain.model.ConfigComponent;
import com.old.silence.config.center.domain.repository.ConfigComponentRepository;
import com.old.silence.config.center.dto.ConfigComponentCommand;
import com.old.silence.config.center.dto.ConfigComponentQuery;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ConfigComponentResource {

    private final ConfigComponentRepository configComponentRepository;
    private final ConfigComponentMapper configComponentMapper;

    public ConfigComponentResource(ConfigComponentRepository configComponentRepository,
                                   ConfigComponentMapper configComponentMapper) {
        this.configComponentRepository = configComponentRepository;
        this.configComponentMapper = configComponentMapper;
    }

    @GetMapping(value = "/configComponents", params = {"subsystemId"})
    public List<ConfigComponent> query(ConfigComponentQuery query) {
        QueryWrapper<ConfigComponent> queryWrapper = QueryWrapperConverter.convert(query, ConfigComponent.class);
        return configComponentRepository.findBySubsystemId(queryWrapper);
    }
    @PostMapping("/configComponents")
    public void create(@RequestBody ConfigComponentCommand configComponentCommand) {
        var configComponent = configComponentMapper.convert(configComponentCommand);
        configComponentRepository.create(configComponent);
    }

    @PutMapping("/configComponents/{id}")
    public void update(@PathVariable BigInteger id, @RequestBody ConfigComponentCommand configComponentCommand) {
        var configComponent = configComponentMapper.convert(configComponentCommand);
        configComponent.setId(id);
        configComponentRepository.update(configComponent);
    }

    @DeleteMapping("/configComponents/{id}")
    public void delete(@PathVariable BigInteger id) {
        configComponentRepository.deleteById(id);
    }

}