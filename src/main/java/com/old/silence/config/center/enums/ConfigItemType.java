package com.old.silence.config.center.enums;


import com.old.silence.core.enums.DescribedEnumValue;

/**
 * @author moryzang
 */
public enum ConfigItemType implements DescribedEnumValue<Byte> {

    PRIVATE(1, "私有"),
    PUBLIC(2, "公开"),
    ;

    private final Byte value;
    private final String description;

    ConfigItemType(int value, String description) {
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
