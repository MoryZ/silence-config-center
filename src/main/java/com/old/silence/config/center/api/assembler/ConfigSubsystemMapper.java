package com.old.silence.config.center.api.assembler;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import com.old.silence.auth.center.api.config.AuthCenterMapStructSpringConfig;
import com.old.silence.config.center.domain.model.ConfigSubsystem;
import com.old.silence.config.center.dto.ConfigSubsystemCommand;

/**
 * @author MurrayZhang
 */

@Mapper(uses = AuthCenterMapStructSpringConfig.class)
public interface ConfigSubsystemMapper extends Converter<ConfigSubsystemCommand, ConfigSubsystem> {


}