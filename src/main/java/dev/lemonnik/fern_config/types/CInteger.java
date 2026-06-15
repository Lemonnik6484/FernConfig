package dev.lemonnik.fern_config.types;

import dev.lemonnik.fern_config.CExporter;
import dev.lemonnik.fern_config.utils.CValue;

public class CInteger extends CValue<Integer> {
    private final int defaultValue;
    private int value;

    public CInteger(String key, String comment, int defaultValue) {
        super(key, comment);
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    @Override
    public Integer get() {
        return value;
    }

    @Override
    public void set(Integer value) {
        this.value = value;
    }

    @Override
    public boolean isDefault() {
        return value == defaultValue;
    }

    @Override
    public String[] getExportStrings(CExporter.Format format) {
        StringBuilder sb = new StringBuilder();

        if (format == CExporter.Format.JSON5) {
            sb.append("// ").append(comment).append("\n");
            sb.append("\"").append(key).append("\": ").append(value).append(",\n");
        } else if (format == CExporter.Format.TOML) {
            sb.append("# ").append(comment).append("\n");
            sb.append(key).append(" = ").append(value).append("\n");
        }

        return sb.toString().split("\n");
    }

    @Override
    public String prefix() {
        return "I";
    }
}
