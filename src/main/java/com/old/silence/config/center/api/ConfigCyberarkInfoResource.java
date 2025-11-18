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
import com.old.silence.config.center.api.assembler.ConfigCyberarkInfoMapper;
import com.old.silence.config.center.domain.service.ConfigCyberarkInfoService;
import com.old.silence.config.center.domain.model.ConfigCyberarkInfo;
import com.old.silence.config.center.domain.repository.ConfigCyberarkInfoRepository;
import com.old.silence.config.center.dto.ConfigCyberarkInfoCommand;
import com.old.silence.config.center.dto.ConfigCyberarkInfoQuery;
import com.old.silence.config.center.dto.ConfigCyberarkInfoRequest;
import com.old.silence.config.center.vo.ConfigCyberarkInfoVo;
import com.old.silence.data.commons.converter.QueryWrapperConverter;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/v1")
public class ConfigCyberarkInfoResource {

    private final ConfigCyberarkInfoService configCyberarkInfoService;
    private final ConfigCyberarkInfoRepository configCyberarkInfoRepository;
    private final ConfigCyberarkInfoMapper configCyberarkInfoMapper;

    public ConfigCyberarkInfoResource(ConfigCyberarkInfoService configCyberarkInfoService,
                                      ConfigCyberarkInfoRepository configCyberarkInfoRepository,
                                      ConfigCyberarkInfoMapper configCyberarkInfoMapper) {
        this.configCyberarkInfoService = configCyberarkInfoService;
        this.configCyberarkInfoRepository = configCyberarkInfoRepository;
        this.configCyberarkInfoMapper = configCyberarkInfoMapper;
    }

    @PostMapping(value = "/configCyberarkInfos/pwd/getPassword")
    public ConfigCyberarkInfoVo findByRemoteRequest(@RequestBody ConfigCyberarkInfoRequest configCyberarkInfoRequest) {
        return configCyberarkInfoService.findByRemoteRequest(configCyberarkInfoRequest);
    }

    @GetMapping(value = "/configCyberarkInfos", params = {"pageNo", "pageSize" })
    public Page<ConfigCyberarkInfo> query(Page<ConfigCyberarkInfo> page, ConfigCyberarkInfoQuery query) {
        var queryWrapper = QueryWrapperConverter.convert(query, ConfigCyberarkInfo.class);
        return configCyberarkInfoRepository.query(page, queryWrapper);
    }

    @PostMapping("/configCyberarkInfos")
    public BigInteger create(@RequestBody ConfigCyberarkInfoCommand configCyberarkInfoCommand) {
        var configCyberarkInfo = configCyberarkInfoMapper.convert(configCyberarkInfoCommand);
        configCyberarkInfoService.create(configCyberarkInfo);
        return configCyberarkInfo.getId();
    }

    @PutMapping("/configCyberarkInfos/{id}")
    public void update(@PathVariable BigInteger id, @RequestBody ConfigCyberarkInfoCommand configCyberarkInfoCommand) {
        var configCyberarkInfo = configCyberarkInfoMapper.convert(configCyberarkInfoCommand);
        configCyberarkInfo.setId(id);
        configCyberarkInfoService.update(configCyberarkInfo);
    }

    @PutMapping("/configCyberarkInfos/{id}/enable")
    public void enable(@PathVariable BigInteger id) {
        configCyberarkInfoRepository.updateEnabled(true, id);
    }

    @PutMapping("/configCyberarkInfos/{id}/disable")
    public void disable(@PathVariable BigInteger id) {
        configCyberarkInfoRepository.updateEnabled(false, id);
    }

    @DeleteMapping("/configCyberarkInfos/{id}")
    public void delete(@PathVariable BigInteger id) {
        configCyberarkInfoRepository.deleteById(id);
    }

}