package com.old.silence.config.center.dto;


import com.old.silence.data.commons.annotation.RelationalQueryProperty;
import com.old.silence.data.commons.converter.Part;

/**
 * @author moryzang
 */
public class ConfigSubsystemQuery  {
    @RelationalQueryProperty(type = Part.Type.STARTING_WITH)
    private String name;
    @RelationalQueryProperty(type = Part.Type.STARTING_WITH)
    private String code;
    @RelationalQueryProperty(type = Part.Type.STARTING_WITH)
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
