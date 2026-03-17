package com.old.silence.config.center.enums;


import com.old.silence.core.enums.DescribedEnumValue;

/**
 * @author moryzang
 */
public enum CloneMode implements DescribedEnumValue<Byte> {

    OVERWRITE_FILES(1, "覆盖文件"),
    SKIP_THE_FILE_OF_THE_SAME_NAME(2, "跳过同名文件"),
    ;

    private final Byte value;
    private final String description;

    CloneMode(int value, String description) {
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
