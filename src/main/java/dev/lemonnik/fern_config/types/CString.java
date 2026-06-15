package dev.lemonnik.fern_config.types;

import dev.lemonnik.fern_config.CExporter;
import dev.lemonnik.fern_config.utils.CValue;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class CString extends CValue<String> {
    private final String defaultValue;
    private String value;

    public CString(String key, String comment, String defaultValue) {
        super(key, comment);

        this.value = defaultValue;
        this.defaultValue = defaultValue;
    }

    @Override
    public String get() {
        return value;
    }

    @Override
    public void set(String value) {
        this.value = value;
    }

    @Override
    public boolean isDefault() {
        return Objects.equals(value, defaultValue);
    }

    private static String escape(@NonNull String input) {
        return input
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
    }

    @Override
    public String[] getExportStrings(CExporter.Format format) {
        StringBuilder sb = new StringBuilder();
        String safeString = escape(value);

        if (format == CExporter.Format.JSON5) {
            sb.append("// ").append(comment).append("\n");
            sb.append("\"").append(key).append("\": \"").append(safeString).append("\",\n");
        } else if (format == CExporter.Format.TOML) {
            sb.append("# ").append(comment).append("\n");
            sb.append(key).append(" = \"").append(safeString).append("\"\n");
        }

        return sb.toString().split("\n");
    }

    @Override
    public String prefix() {
        return "S";
    }
}
