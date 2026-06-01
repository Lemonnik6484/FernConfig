package dev.lemonnik.fern_config.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record CMap(Map<CCategory, List<CValue<?>>> map) {
    public CMap() {
        this(new LinkedHashMap<>());
    }
}
