package com.old.silence.config.center.enums;


import com.old.silence.core.enums.DescribedEnumValue;

/**
 * @author moryzang
 */
public enum OperationType implements DescribedEnumValue<Byte> {

    ADD(1, "添加"),
    DELETE(2, "删除"),
    UPDATE(3, "修改"),
    ;

    private final Byte value;
    private final String description;

    OperationType(int value, String description) {
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
