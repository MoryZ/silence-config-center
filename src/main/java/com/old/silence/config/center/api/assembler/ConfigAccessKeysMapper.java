package com.old.silence.config.center.api.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import com.old.silence.config.center.domain.model.ConfigAccessKeys;
import com.old.silence.config.center.dto.ConfigAccessKeysCommand;
import com.old.silence.core.mapstruct.MapStructSpringConfig;

/**
 * @author MurrayZhang
 */

@Mapper(uses = MapStructSpringConfig.class)
public interface ConfigAccessKeysMapper extends Converter<ConfigAccessKeysCommand, ConfigAccessKeys> {


    @Mapping(target = "enabled", constant = "true")
    ConfigAccessKeys convert(ConfigAccessKeysCommand command) ;
}