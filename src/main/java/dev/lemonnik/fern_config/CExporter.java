package dev.lemonnik.fern_config;

import dev.lemonnik.fern_config.utils.CMap;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class CExporter {
    public enum Format {
        JSON5(".json5"),
        TOML(".toml");

        private final String extension;

        Format(String extension) {
            this.extension = extension;
        }

        public String extension() {
            return extension;
        }
    }

    public static CMap load(CMap defaults, String fileName, Format format) {
        Path path = configPath(fileName, format);

        return null;
    }

    public static boolean save(CMap config, String fileName, Format format) {
        Path path = configPath(fileName, format);

        return false;
    }

    private static Path configPath(String fileName, Format format) {
        return FabricLoader.getInstance().getConfigDir()
                .toAbsolutePath()
                .resolve(fileName + format.extension());
    }
}