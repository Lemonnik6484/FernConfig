package dev.lemonnik.fern_config.types;

import dev.lemonnik.fern_config.CExporter;
import dev.lemonnik.fern_config.utils.CValue;
import dev.lemonnik.fern_config.utils.IntArray;

import java.util.Objects;

public class CIntArray extends CValue<IntArray> {
    private final IntArray defaultValues;
    private IntArray values;

    public CIntArray(String key, String comment, IntArray defaultValues) {
        super(key, comment);
        this.defaultValues = defaultValues;
        this.values = defaultValues;
    }

    @Override
    public IntArray get() {
        return values;
    }

    @Override
    public void set(IntArray values) {
        this.values = values;
    }

    public void add(int value) {
        values.values().add(value);
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
            for (int value : values.values()) {
                sb.append("    ").append(value).append(",\n");
            }
            sb.append("],\n");
        } else if (format == CExporter.Format.TOML) {
            sb.append("# ").append(comment).append("\n");
            sb.append(key).append(" = [\n");
            for (int value : values.values()) {
                sb.append("    ").append(value).append(",\n");
            }
            sb.append("]\n");
        }

        return sb.toString().split("\n");
    }

    @Override
    public String prefix() {
        return "A_I";
    }
}
