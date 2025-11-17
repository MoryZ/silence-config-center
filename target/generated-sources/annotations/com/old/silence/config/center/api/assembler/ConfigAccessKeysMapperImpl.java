package com.old.silence.config.center.api.assembler;

import com.old.silence.config.center.domain.model.ConfigAccessKeys;
import com.old.silence.config.center.dto.ConfigAccessKeysCommand;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class ConfigAccessKeysMapperImpl implements ConfigAccessKeysMapper {

    @Override
    public ConfigAccessKeys convert(ConfigAccessKeysCommand command) {
        if ( command == null ) {
            return null;
        }

        ConfigAccessKeys configAccessKeys = new ConfigAccessKeys();

        configAccessKeys.setAccessKey( command.getAccessKey() );
        configAccessKeys.setSecretKey( command.getSecretKey() );
        configAccessKeys.setComponentCode( command.getComponentCode() );
        configAccessKeys.setDescription( command.getDescription() );

        configAccessKeys.setEnabled( true );

        return configAccessKeys;
    }
}
