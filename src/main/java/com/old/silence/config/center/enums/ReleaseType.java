package com.old.silence.config.center.enums;


import com.old.silence.core.enums.DescribedEnumValue;

/**
 * @author moryzang
 */
public enum ReleaseType implements DescribedEnumValue<Byte> {

    NORMAL(1, "普通发布"),
    GRAY(2, "灰度发布"),
    ;

    private final Byte value;
    private final String description;

    ReleaseType(int value, String description) {
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
