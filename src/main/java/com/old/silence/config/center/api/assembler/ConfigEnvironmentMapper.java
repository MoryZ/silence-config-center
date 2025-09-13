package com.old.silence.config.center.api.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import com.old.silence.config.center.domain.model.ConfigEnvironment;
import com.old.silence.config.center.dto.ConfigEnvironmentCommand;
import com.old.silence.core.mapstruct.MapStructSpringConfig;

/**
 * @author MurrayZhang
 */

@Mapper(uses = MapStructSpringConfig.class)
public interface ConfigEnvironmentMapper extends Converter<ConfigEnvironmentCommand, ConfigEnvironment> {


    @Mapping(target = "display", constant = "true")
    ConfigEnvironment convert(ConfigEnvironmentCommand command) ;
}