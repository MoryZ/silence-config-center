package com.old.silence.config.center.api.assembler;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import com.old.silence.auth.center.api.config.AuthCenterMapStructSpringConfig;
import com.old.silence.config.center.domain.model.ConfigItem;
import com.old.silence.config.center.dto.ConfigItemCommand;

/**
 * @author MurrayZhang
 */

@Mapper(uses = AuthCenterMapStructSpringConfig.class)
public interface ConfigItemMapper extends Converter<ConfigItemCommand, ConfigItem> {


}