package dev.lemonnik.fern_config.types;

import dev.lemonnik.fern_config.CExporter;
import dev.lemonnik.fern_config.utils.CValue;
import dev.lemonnik.fern_config.utils.FloatArray;

import java.util.Objects;

public class CFloatArray extends CValue<FloatArray> {
    private final FloatArray defaultValues;
    private FloatArray values;

    public CFloatArray(String key, String comment, FloatArray defaultValues) {
        super(key, comment);
        this.defaultValues = defaultValues;
        this.values = defaultValues;
    }

    @Override
    public FloatArray get() {
        return values;
    }

    @Override
    public void set(FloatArray values) {
        this.values = values;
    }

    @Override
    public boolean isDefault() {
        return Objects.equals(values, defaultValues);
    }

    @Override
    public String[] getExportStrings(CExporter.Format format) {
        StringBuilder sb = new StringBuilder();

        if (format == CExporter.Format.JSON5) {
            sb.append("// ").append(comment).append("\n");
            sb.append("\"").append(key).append("\": [\n");
            for (float value : values.values()) {
                sb.append("    ").append(value).append(",\n");
            }
            sb.append("],\n");
        } else if (format == CExporter.Format.TOML) {
            sb.append("# ").append(comment).append("\n");
            sb.append(key).append(" = [\n");
            for (float value : values.values()) {
                sb.append("    ").append(value).append(",\n");
            }
            sb.append("]\n");
        }

        return sb.toString().split("\n");
    }

    @Override
    public String prefix() {
        return "A_F";
    }
}
