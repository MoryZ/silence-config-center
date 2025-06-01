package com.old.silence.config.center.api;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.old.silence.data.commons.converter.QueryWrapperConverter;
import com.old.silence.config.center.api.assembler.ConfigEnvironmentMapper;
import com.old.silence.config.center.domain.model.ConfigEnvironment;
import com.old.silence.config.center.domain.repository.ConfigEnvironmentRepository;
import com.old.silence.config.center.dto.ConfigEnvironmentCommand;
import com.old.silence.config.center.dto.ConfigEnvironmentQuery;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ConfigEnvironmentResource {

    private final ConfigEnvironmentRepository configEnvironmentRepository;
    private final ConfigEnvironmentMapper configEnvironmentMapper;

    public ConfigEnvironmentResource(ConfigEnvironmentRepository configEnvironmentRepository,
                                     ConfigEnvironmentMapper configEnvironmentMapper) {
        this.configEnvironmentRepository = configEnvironmentRepository;
        this.configEnvironmentMapper = configEnvironmentMapper;
    }

    @GetMapping(value = "/configEnvironments", params = {"configComponentId", "envType" })
    public List<ConfigEnvironment> query(ConfigEnvironmentQuery query) {
        var queryWrapper = QueryWrapperConverter.convert(query, ConfigEnvironment.class);
        return configEnvironmentRepository.query(queryWrapper);
    }

    @PostMapping("/configEnvironments")
    public BigInteger create(@RequestBody ConfigEnvironmentCommand configEnvironmentCommand) {
        var configEnvironment = configEnvironmentMapper.convert(configEnvironmentCommand);
        configEnvironmentRepository.create(configEnvironment);
        return configEnvironment.getId();
    }

    @PutMapping("/configEnvironments/{id}")
    public void update(@PathVariable BigInteger id, @RequestBody ConfigEnvironmentCommand configEnvironmentCommand) {
        var configEnvironment = configEnvironmentMapper.convert(configEnvironmentCommand);
        configEnvironment.setId(id);
        configEnvironmentRepository.update(configEnvironment);
    }

    @DeleteMapping("/configEnvironments/{id}")
    public void delete(@PathVariable BigInteger id) {
        configEnvironmentRepository.deleteById(id);
    }

}