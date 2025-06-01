package com.old.silence.config.center.api.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import com.old.silence.auth.center.api.config.AuthCenterMapStructSpringConfig;
import com.old.silence.config.center.domain.model.ConfigComponent;
import com.old.silence.config.center.dto.ConfigComponentCommand;

/**
 * @author MurrayZhang
 */

@Mapper(uses = AuthCenterMapStructSpringConfig.class)
public interface ConfigComponentMapper extends Converter<ConfigComponentCommand, ConfigComponent> {


    @Mapping(target = "status", constant = "true")
    ConfigComponent convert(ConfigComponentCommand command) ;
}