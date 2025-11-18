package com.old.silence.config.center.api.assembler;

import com.old.silence.config.center.domain.model.ConfigCyberarkInfo;
import com.old.silence.config.center.dto.ConfigCyberarkInfoCommand;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class ConfigCyberarkInfoMapperImpl implements ConfigCyberarkInfoMapper {

    @Override
    public ConfigCyberarkInfo convert(ConfigCyberarkInfoCommand command) {
        if ( command == null ) {
            return null;
        }

        ConfigCyberarkInfo configCyberarkInfo = new ConfigCyberarkInfo();

        configCyberarkInfo.setCyberarkObject( command.getCyberarkObject() );
        configCyberarkInfo.setEncryptedValue( command.getEncryptedValue() );
        configCyberarkInfo.setComponentCode( command.getComponentCode() );
        configCyberarkInfo.setAppKey( command.getAppKey() );
        configCyberarkInfo.setSafe( command.getSafe() );
        configCyberarkInfo.setFolder( command.getFolder() );
        configCyberarkInfo.setDescription( command.getDescription() );

        configCyberarkInfo.setEnabled( true );

        return configCyberarkInfo;
    }
}
