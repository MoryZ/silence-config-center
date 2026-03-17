package com.old.silence.config.center.enums;


import com.old.silence.core.enums.DescribedEnumValue;

/**
 * @author moryzang
 */
public enum DistributionStrategy implements DescribedEnumValue<Byte> {

    IMMEDIATE_RELEASE(1, "立即下发"),
    SCHEDULED_RELEASE(2, "自定义时间"),
    ;

    private final Byte value;
    private final String description;

    DistributionStrategy(int value, String description) {
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
