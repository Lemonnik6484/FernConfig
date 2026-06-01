package dev.lemonnik.fern_config.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public record CMap(Map<CCategory, LinkedHashMap<String, CValue<?>>> map) {
    public CMap() {
        this(new LinkedHashMap<>());
    }
}