package dev.lemonnik.fern_config;

import dev.lemonnik.fern_config.utils.CValue;
import dev.lemonnik.fern_config.utils.CCategory;
import dev.lemonnik.fern_config.utils.CMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class CConfig {
    private final CMap categories = new CMap();

    protected <T extends CValue<?>> T register(CCategory category, T value) {
        List<CValue<?>> values = categories.map().computeIfAbsent(category, k -> new ArrayList<>());
        for (int i = 0; i < values.size(); i++) {
            if (Objects.equals(values.get(i).key, value.key)) {
                values.set(i, value);
                return value;
            }
        }
        values.add(value);
        return value;
    }

    public Map<CCategory, List<CValue<?>>> getCategories() {
        return categories.map();
    }

    public List<CValue<?>> getValues(CCategory category) {
        return categories.map().get(category);
    }

    public abstract @NotNull String getFileName();

    protected abstract @NotNull CExporter.Format getFormat();

    public CValue<?> getValue(String key) {
        for (List<CValue<?>> values : categories.map().values()) {
            for (CValue<?> value : values) {
                if (Objects.equals(value.key, key)) {
                    return value;
                }
            }
        }
        return null;
    }

    public boolean reload() {
        CMap loaded = CExporter.load(categories, getFileName(), getFormat());
        if (!loaded.map().isEmpty()) {
            this.categories.map().putAll(loaded.map());
            return CExporter.save(loaded, getFileName(), getFormat());
        } else {
            return false;
        }
    }

    public boolean save() {
        return CExporter.save(categories, getFileName(), getFormat());
    }
}
