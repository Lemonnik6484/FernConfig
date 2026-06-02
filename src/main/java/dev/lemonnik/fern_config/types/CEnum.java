package dev.lemonnik.fern_config.types;

import dev.lemonnik.fern_config.CExporter;
import dev.lemonnik.fern_config.utils.CValue;

import java.util.Arrays;

public class CEnum<T extends Enum<T>> extends CValue<T> {
    private final Class<T> enumClass;
    private final T defaultValue;
    private T value;

    public CEnum(String key, String comment, Class<T> enumClass, T defaultValue) {
        super(key, comment);
        this.enumClass = enumClass;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void set(T value) {
        this.value = value;
    }

    public void setEnumStr(String enumStr) {
        this.value = Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.name().equalsIgnoreCase(enumStr))
                .findFirst()
                .orElse(defaultValue);
    }

    @Override
    public boolean isDefault() {
        return value == defaultValue;
    }

    @Override
    public String[] getExportStrings(CExporter.Format format) {
        StringBuilder sb = new StringBuilder();

        if (format == CExporter.Format.JSON5) {
            sb.append("/*\n");
            sb.append(comment).append('\n');
            sb.append("Possible values:\n");
            for (Enum<?> enumValue : enumClass.getEnumConstants()) {
                sb.append("\"").append(enumValue.name()).append("\"\n");
            }
            sb.append("*/\n");
            sb.append("\"").append(key).append("\": ").append("\"").append(get().toString()).append("\",\n");
        } else if (format == CExporter.Format.TOML) {
            sb.append("# ").append(comment).append("\n");
            sb.append("# ").append("Possible values:\n");
            for (Enum<?> enumValue : getEnumClass().getEnumConstants()) {
                sb.append("# ").append(enumValue.name()).append("\n");
            }
            sb.append(key).append(" = \"").append(get().toString()).append("\"\n");
        }

        return sb.toString().split("\n");
    }

    public Class<T> getEnumClass() {
        return enumClass;
    }
}
