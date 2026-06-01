package dev.lemonnik.fern_config.types;

import dev.lemonnik.fern_config.utils.CValue;

public class CBoolean extends CValue<Boolean> {
    private final boolean defaultValue;
    private Boolean value;

    public CBoolean(String key, String comment, boolean defaultValue) {
        super(key, comment);

        this.value = defaultValue;
        this.defaultValue = defaultValue;
    }

    public void toggle() {
        this.value = !value;
    }

    @Override
    public Boolean get() {
        return value;
    }

    @Override
    public void set(Boolean value) {
        this.value = value;
    }

    @Override
    public boolean isDefault() {
        return value == defaultValue;
    }
}
