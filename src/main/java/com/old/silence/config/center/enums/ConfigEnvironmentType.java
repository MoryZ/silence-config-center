package com.old.silence.config.center.enums;


import com.old.silence.core.enums.DescribedEnumValue;

/**
 * @author moryzang
 */
public enum ConfigEnvironmentType implements DescribedEnumValue<Byte> {

    CI(1, "CI"),
    STG(2, "STG"),
    PRD(3, "PRD"),
    ;

    private final Byte value;
    private final String description;

    ConfigEnvironmentType(int value, String description) {
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
