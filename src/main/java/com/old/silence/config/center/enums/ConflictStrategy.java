package com.old.silence.config.center.enums;

import com.old.silence.core.enums.DescribedEnumValue;

/**
 * @author moryzang
 */
public enum ConflictStrategy implements DescribedEnumValue<Byte> {
    TERMINATE(1, "终止"),
    SKIP_THE_FILE_OF_THE_SAME_NAME(2, "跳过同名文件"),
    OVERWRITE_FILES(3, "覆盖文件"),
    ;

    private final Byte value;
    private final String description;

    ConflictStrategy(int value, String description) {
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