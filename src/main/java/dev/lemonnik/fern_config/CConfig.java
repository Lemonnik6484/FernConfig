package dev.lemonnik.fern_config;

import dev.lemonnik.fern_config.utils.CValue;
import dev.lemonnik.fern_config.utils.CCategory;
import dev.lemonnik.fern_config.utils.CMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class CConfig {
    private final CMap cMap = new CMap();

    protected <T extends CValue<?>> T register(CCategory category, T value) {
        List<CValue<?>> values = cMap.map().computeIfAbsent(category, k -> new ArrayList<>());
        for (int i = 0; i < values.size(); i++) {
            if (Objects.equals(values.get(i).key, value.key)) {
                values.set(i, value);
                return value;
            }
        }
        values.add(value);
        return value;
    }

    public CMap getcMap() {
        return cMap;
    }

    public List<CValue<?>> getValues(CCategory category) {
        return cMap.map().get(category);
    }

    public abstract @NotNull String getFileName();

    protected abstract @NotNull CExporter.Format getFormat();

    public CValue<?> getValue(CCategory category, String key) {
        for (CValue<?> value : getValues(category)) {
            if (value.key.equals(key)) {
                return value;
            }
        }
        return null;
    }

    public CValue<?> getValue(String key) {
        for (List<CValue<?>> values : cMap.map().values()) {
            for (CValue<?> value : values) {
                if (value.key.equals(key)) {
                    return value;
                }
            }
        }
        return null;
    }

    public boolean reload() {
        CConfig config = CExporter.load(this, getFileName(), getFormat());
        if (!config.getcMap().map().isEmpty()) {
            this.cMap.map().putAll(config.getcMap().map());
            return CExporter.save(this);
        } else {
            return false;
        }
    }

    public boolean save() {
        return CExporter.save(this);
    }
}
