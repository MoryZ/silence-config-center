package com.old.silence.config.center.api.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import com.old.silence.auth.center.api.config.AuthCenterMapStructSpringConfig;
import com.old.silence.config.center.domain.model.ConfigEnvironment;
import com.old.silence.config.center.dto.ConfigEnvironmentCommand;

/**
 * @author MurrayZhang
 */

@Mapper(uses = AuthCenterMapStructSpringConfig.class)
public interface ConfigEnvironmentMapper extends Converter<ConfigEnvironmentCommand, ConfigEnvironment> {


    @Mapping(target = "display", constant = "true")
    ConfigEnvironment convert(ConfigEnvironmentCommand command) ;
}