package dev.lemonnik.fern_config.impl;

import dev.lemonnik.fern_config.CExporter;
import org.jetbrains.annotations.NotNull;

public class TestTOMLConfig extends TestJSON5Config {
    @Override
    protected @NotNull CExporter.Format getFormat() {
        return CExporter.Format.TOML;
    }
}
