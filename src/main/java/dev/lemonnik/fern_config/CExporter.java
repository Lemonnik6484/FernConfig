package dev.lemonnik.fern_config;

import dev.lemonnik.fern_config.types.*;
import dev.lemonnik.fern_config.utils.CCategory;
import dev.lemonnik.fern_config.utils.CMap;
import dev.lemonnik.fern_config.utils.CValue;
import dev.lemonnik.fern_config.utils.MaskType;

//? if fabric
import net.fabricmc.loader.api.FabricLoader;

//? if neoforge
//import net.neoforged.fml.loading.FMLPaths;

//? if forge
//import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static CMap load(CConfig config, String fileName, Format format) {
        Path path = configPath(fileName, format);
        String[] content;
        CMap defaultMap = config.getcMap();

        String maskKey = "";

        try {
            content = Files.readAllLines(path).toArray(new String[0]);
        } catch (Exception e) {
            if (e instanceof NoSuchFileException) {
                FernConfig.LOGGER.warn("Config file {}{} not found, falling back to defaults", fileName, format.extension());
                return defaultMap;
            }

            FernConfig.LOGGER.error("Error reading config file: {}", fileName);
            e.printStackTrace();
            return defaultMap;
        }

        Pattern valuePattern = Pattern.compile("\"?([^\"\\s=]+)\"?\\s*[:=]\\s*([^,]+)");

        for (String line : content) {
            if (!maskKey.isEmpty() && (line.startsWith("\"") && line.endsWith("\","))) {
                if (config.getValue(maskKey) instanceof CMask cMask) {
                    line = line.substring(1, line.length() - 2);
                    cMask.addMaskStr(line);
                    continue;
                }
            }

            Matcher valueMatcher = valuePattern.matcher(line);
            if (valueMatcher.find()) {
                String key = valueMatcher.group(1);
                String value = valueMatcher.group(2);

                String prefix = key.split("_")[0];

                try {
                    switch (prefix) {
                        case "B": {
                            if (config.getValue(key) instanceof CBoolean cBoolean) {
                                cBoolean.set(Boolean.parseBoolean(value));
                            }
                            break;
                        }
                        case "E": {
                            value = value.substring(1, value.length() - 1);
                            if (!maskKey.isEmpty()) {
                                if (config.getValue(maskKey) instanceof CMask cMask) {
                                    cMask.setMaskType(MaskType.fromString(value));
                                    break;
                                }
                            }

                            if (config.getValue(key) instanceof CEnum<?> cEnum) {
                                cEnum.setEnumStr(value);
                            }
                            break;
                        }
                        case "M": {
                            maskKey = key;
                            break;
                        }
                        case "I": {
                            if (config.getValue(key) instanceof CInteger cInteger) {
                                cInteger.set(Integer.parseInt(value));
                            }
                            break;
                        }
                        case "F": {
                            if (config.getValue(key) instanceof CFloat cFloat) {
                                cFloat.set(Float.parseFloat(value));
                            }
                        }
                        case "D": {
                            if (config.getValue(key) instanceof CDouble cDouble) {
                                cDouble.set(Double.parseDouble(value));
                            }
                        }
                        case "S": {
                            if (config.getValue(key) instanceof CString cString) {
                                cString.set(value.substring(1, value.length() - 1));
                            }
                        }
                    }
                } catch (Exception e) {
                    FernConfig.LOGGER.warn("Error reading config file: {}", fileName);
                    e.printStackTrace();
                }
            }
        }

        return defaultMap;
    }

    public static boolean save(CMap config, String fileName, Format format) {
        Path path = configPath(fileName, format);
        StringBuilder sb = new StringBuilder();
        String tab = "    ";
        int currTab = 1;

        
        if (format == Format.JSON5) {
            sb.append("{\n");
            for (CCategory category : config.map().keySet()) {
                sb.append(tab.repeat(currTab)).append("/*\n");
                for (String comment : category.description()) {
                    sb.append(tab.repeat(currTab)).append(comment).append('\n');
                }
                sb.append(tab.repeat(currTab)).append("*/\n");

                sb.append(tab.repeat(currTab)).append("\"").append(category.id()).append("\": {\n");
                currTab++;

                for (CValue<?> value : config.map().get(category)) {
                    for (String string : value.getExportStrings(format)) {
                        sb.append(tab.repeat(currTab)).append(string).append("\n");
                    }
                }

                currTab--;
                sb.append(tab.repeat(currTab)).append("},\n");
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
                        sb.append(tab.repeat(currTab)).append(string).append("\n");
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
        //? if fabric {
        return FabricLoader.getInstance().getConfigDir()
                .toAbsolutePath()
                .resolve(fileName + format.extension());
        //?} else if forge || neoforge {
        /*return FMLPaths.CONFIGDIR.get();
        *///?}
    }
}
