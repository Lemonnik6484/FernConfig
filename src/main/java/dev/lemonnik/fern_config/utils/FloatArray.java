package dev.lemonnik.fern_config.utils;

import java.util.List;

public record FloatArray(List<Float> values) {
    public FloatArray(List<Float> values) {
        this.values = values;
    }

    public Float[] toArray() {
        return values.toArray(new Float[0]);
    }
}