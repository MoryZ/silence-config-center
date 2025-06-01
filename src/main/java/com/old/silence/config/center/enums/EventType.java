package com.old.silence.config.center.enums;


import com.old.silence.core.enums.DescribedEnumValue;

/**
 * @author moryzang
 */
public enum EventType implements DescribedEnumValue<Byte> {

    /**
     * 发布事件
     */
    PUBLISH(1,"PUBLISH"),

    /**
     * 删除事件
     */
    REMOVE(2,"REMOVE"),
    /**
     * 忽略事件
     */
    IGNORE(3,"IGNORE"),
    ;
    ;

    private final Byte value;
    private final String description;

    EventType(int value, String description) {
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