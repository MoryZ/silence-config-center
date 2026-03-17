package com.old.silence.config.center.api.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import com.old.silence.config.center.domain.model.ConfigCyberarkInfo;
import com.old.silence.config.center.dto.ConfigCyberarkInfoCommand;
import com.old.silence.core.mapstruct.MapStructSpringConfig;

/**
 * @author MurrayZhang
 */

@Mapper(uses = MapStructSpringConfig.class)
public interface ConfigCyberarkInfoMapper extends Converter<ConfigCyberarkInfoCommand, ConfigCyberarkInfo> {


    @Mapping(target = "enabled", constant = "true")
    ConfigCyberarkInfo convert(ConfigCyberarkInfoCommand command) ;
}