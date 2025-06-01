package com.old.silence.config.center.enums;


import com.old.silence.core.enums.DescribedEnumValue;

/**
 * @author moryzang
 */
public enum NameSpaceStatus implements DescribedEnumValue<Byte> {

    SAVED(1, "已保存"),
    PUBLISHED(2, "已发布"),
    ;

    private final Byte value;
    private final String description;

    NameSpaceStatus(int value, String description) {
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
