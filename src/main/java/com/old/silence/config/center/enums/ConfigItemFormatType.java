package com.old.silence.config.center.enums;


import com.old.silence.core.enums.DescribedEnumValue;

/**
 * @author moryzang
 */
public enum ConfigItemFormatType implements DescribedEnumValue<Byte> {

    YML(1, "YML"),
    PROPERTIES(2, "PROPERTIES"),
    TXT(3, "TXT"),
    ;

    private final Byte value;
    private final String description;

    ConfigItemFormatType(int value, String description) {
           this.value = (byte) value;
           this.description = description;
    }

    @Override
    public Byte getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
