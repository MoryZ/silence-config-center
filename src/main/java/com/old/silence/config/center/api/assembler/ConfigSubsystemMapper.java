package com.old.silence.config.center.api.assembler;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import com.old.silence.config.center.domain.model.ConfigSubsystem;
import com.old.silence.config.center.dto.ConfigSubsystemCommand;
import com.old.silence.core.mapstruct.MapStructSpringConfig;

/**
 * @author MurrayZhang
 */

@Mapper(uses = MapStructSpringConfig.class)
public interface ConfigSubsystemMapper extends Converter<ConfigSubsystemCommand, ConfigSubsystem> {


}