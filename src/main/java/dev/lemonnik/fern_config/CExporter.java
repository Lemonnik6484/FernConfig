package dev.lemonnik.fern_config;

import dev.lemonnik.fern_config.types.CBoolean;
import dev.lemonnik.fern_config.types.CEnum;
import dev.lemonnik.fern_config.types.CMask;
import dev.lemonnik.fern_config.utils.CCategory;
import dev.lemonnik.fern_config.utils.CMap;
import dev.lemonnik.fern_config.utils.CValue;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
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
        StringBuilder sb = new StringBuilder();
        String tab = "   ";
        int currTab = 1;

        
        if (format == Format.JSON5) {
            sb.append("{\n");
            for (CCategory category : config.map().keySet()) {
                sb.repeat(tab, currTab).append("/*\n");
                for (String comment : category.description()) {
                    sb.repeat(tab, currTab).append(comment).append('\n');
                }
                sb.repeat(tab, currTab).append("*/\n");

                sb.repeat(tab, currTab).append("\"").append(category.id()).append("\": {\n");
                currTab++;

                for (CValue<?> value : config.map().get(category)) {
                    for (String string : value.getExportStrings(format)) {
                        sb.repeat(tab, currTab).append(string).append("\n");
                    }
                }

                currTab--;
                sb.repeat(tab, currTab).append("},\n");
            }
            sb.append("}");
        } else if (format == Format.TOML) {
            for (CCategory category : config.map().keySet()) {
                for (String comment : category.description()) {
                    sb.append("# ").append(comment).append("\n");
                }
                sb.append("[").append(category.id()).append("]\n");

                for (CValue<?> value : config.map().get(category)) {
                    for (String string : value.getExportStrings(format)) {
                        sb.repeat(tab, currTab).append(string).append("\n");
                    }
                }
            }
        }

        try {
            Files.writeString(path, sb.toString());
            return true;
        } catch (Exception e) {
            FernConfig.LOGGER.error("Could not save config file {}: {}", path, e.getMessage());
            return false;
        }
    }

    private static Path configPath(String fileName, Format format) {
        return FabricLoader.getInstance().getConfigDir()
                .toAbsolutePath()
                .resolve(fileName + format.extension());
    }
}
