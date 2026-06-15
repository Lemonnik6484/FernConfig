package dev.lemonnik.fern_config.types;

import dev.lemonnik.fern_config.CExporter;
import dev.lemonnik.fern_config.utils.CValue;

public class CDouble extends CValue<Double> {
    private final double defaultValue;
    private double value;

    public CDouble(String key, String comment, Double defaultValue) {
        super(key, comment);
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    @Override
    public Double get() {
        return value;
    }

    @Override
    public void set(Double value) {
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
        return "D";
    }
}
