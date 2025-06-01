package com.old.silence.config.center.api.assembler;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import com.old.silence.auth.center.api.config.AuthCenterMapStructSpringConfig;
import com.old.silence.config.center.domain.model.ConfigItemHistory;
import com.old.silence.config.center.dto.ConfigItemHistoryCommand;

/**
 * @author MurrayZhang
 */

@Mapper(uses = AuthCenterMapStructSpringConfig.class)
public interface ConfigItemHistoryMapper extends Converter<ConfigItemHistoryCommand, ConfigItemHistory> {


}