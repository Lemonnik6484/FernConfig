package dev.lemonnik.fern_config.utils;

import java.util.List;

public record IntArray(List<Integer> values) {
    public Integer[] toArray() {
        return values.toArray(new Integer[0]);
    }
}