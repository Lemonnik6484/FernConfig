package dev.lemonnik.fern_config.utils;

import dev.lemonnik.fern_config.CExporter;

public abstract class CValue<T> {
    public final String key;
    public final String comment;

    public CValue(String key, String comment) {
        this.key = prefix() + "_" + key;
        this.comment = comment;
    }

    public abstract T get();
    public abstract void set(T value);

    public abstract boolean isDefault();

    public abstract String[] getExportStrings(CExporter.Format format);

    public abstract String prefix();
}