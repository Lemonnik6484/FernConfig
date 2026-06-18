package dev.lemonnik.fern_config;

import dev.lemonnik.fern_config.types.*;
import dev.lemonnik.fern_config.utils.*;
import org.jetbrains.annotations.NotNull;

//? if fabric
import net.fabricmc.loader.api.FabricLoader;

//? if neoforge
//import net.neoforged.fml.loading.FMLPaths;

//? if forge
//import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public static CConfig load(CConfig config, String fileName, Format format) {
        Path path = configPath(config);
        String[] content;

        CConfig defaultConfig = new CConfig() {
            @Override
            public @NotNull String getFileName() {
                return fileName;
            }

            @Override
            protected @NotNull CExporter.Format getFormat() {
                return format;
            }
        };
        defaultConfig.getcMap().map().putAll(config.getcMap().map());

        ArrayList<String> currentList = new ArrayList<>();

        String maskKey = "";
        boolean inMaskValues =  false;

        String arrayKey = "";

        try {
            content = Files.readAllLines(path).toArray(new String[0]);

            Pattern valuePattern = Pattern.compile("\"?([^\"\\s=]+)\"?\\s*[:=]\\s*(.*)");

            for (String line : content) {
                line = line.trim();

                // mask
                if (!maskKey.isEmpty()) {
                    //mask end
                    if (line.startsWith("}")) {
                        maskKey = "";
                        continue;
                    }

                    if (inMaskValues) {
                        // values end
                        if (line.startsWith("]")) {
                            inMaskValues = false;
                            if (config.getValue(maskKey) instanceof CMask cMask) {
                                cMask.setMaskStr(currentList);
                                currentList.clear();
                            }
                            continue;
                        }

                        // values
                        line = line.split("\"")[1];
                        currentList.add(line);
                        continue;
                    }
                }

                if (!arrayKey.isEmpty() && line.startsWith("]")) {
                    String arrayType = arrayKey.split("_")[1];

                    switch (arrayType) {
                        case "F": {
                            if (config.getValue(arrayKey) instanceof CFloatArray cFloatArray) {
                                List<Float> floats = currentList.stream()
                                        .map(Float::parseFloat)
                                        .toList();
                                cFloatArray.set(new FloatArray(floats));
                                currentList.clear();
                            }
                            break;
                        }
                    }
                    arrayKey = "";
                    continue;
                }

                if (Objects.equals(line, "\"values\": [") || Objects.equals(line, "values = [")) {
                    inMaskValues = true;
                }

                if (!arrayKey.isEmpty()) {
                    String arrayType = arrayKey.split("_")[1];

                    switch (arrayType) {
                        case "I": {
                            currentList.add(line.substring(0, line.length() - 1));
                            break;
                        }
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
                                    cBoolean.set(Boolean.parseBoolean(value.split(",")[0]));
                                }
                                break;
                            }
                            case "E": {
                                if (!maskKey.isEmpty()) {
                                    if (config.getValue(maskKey) instanceof CMask cMask) {
                                        cMask.setMaskType(MaskType.fromString(value.split("\"")[1]));
                                        break;
                                    }
                                }

                                if (config.getValue(key) instanceof CEnum<?> cEnum) {
                                    cEnum.setEnumStr(value.split("\"")[1]);
                                }
                                break;
                            }
                            case "M": {
                                maskKey = key;
                                break;
                            }
                            case "I": {
                                if (config.getValue(key) instanceof CInteger cInteger) {
                                    cInteger.set(Integer.parseInt(value.split(",")[0]));
                                }
                                break;
                            }
                            case "F": {
                                if (config.getValue(key) instanceof CFloat cFloat) {
                                    cFloat.set(Float.parseFloat(value.split(",")[0]));
                                }
                                break;
                            }
                            case "D": {
                                if (config.getValue(key) instanceof CDouble cDouble) {
                                    cDouble.set(Double.parseDouble(value.split(",")[0]));
                                }
                                break;
                            }
                            case "S": {
                                if (config.getValue(key) instanceof CString cString) {
                                    cString.set(value.split("\"")[1]);
                                }
                                break;
                            }
                            case "A": {
                                arrayKey = key;
                                break;
                            }
                        }
                    } catch (Exception e) {
                        FernConfig.LOGGER.warn("Error reading config file: {}", fileName);
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            if (e instanceof NoSuchFileException) {
                FernConfig.LOGGER.warn("Config file {}{} not found, falling back to defaults", fileName, format.extension());
                return defaultConfig;
            }

            FernConfig.LOGGER.error("Error reading config file: {}", fileName);
            e.printStackTrace();
            return defaultConfig;
        }

        return defaultConfig;
    }

    public static boolean save(CConfig config) {
        CMap map = config.getcMap();
        Format format = config.getFormat();
        Path path = configPath(config);
        StringBuilder sb = new StringBuilder();
        String tab = "    ";
        int currTab = 1;

        
        if (format == Format.JSON5) {
            sb.append("{\n");
            for (CCategory category : map.map().keySet()) {
                sb.append(tab.repeat(currTab)).append("/*\n");
                for (String comment : category.description()) {
                    sb.append(tab.repeat(currTab)).append(comment).append('\n');
                }
                sb.append(tab.repeat(currTab)).append("*/\n");

                sb.append(tab.repeat(currTab)).append("\"").append(category.id()).append("\": {\n");
                currTab++;

                for (CValue<?> value : map.map().get(category)) {
                    for (String string : value.getExportStrings(format)) {
                        sb.append(tab.repeat(currTab)).append(string).append("\n");
                    }
                }

                currTab--;
                sb.append(tab.repeat(currTab)).append("},\n");
            }
            sb.append("}");
        } else if (format == Format.TOML) {
            for (CCategory category : map.map().keySet()) {
                for (String comment : category.description()) {
                    sb.append("# ").append(comment).append("\n");
                }
                sb.append("[").append(category.id()).append("]\n");

                for (CValue<?> value : map.map().get(category)) {
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

    private static Path configPath(CConfig config) {
        String fileName = config.getFileName();
        Format format = config.getFormat();

        //? if fabric {
        return FabricLoader.getInstance().getConfigDir()
                .toAbsolutePath()
                .resolve(fileName + format.extension());
        //?} else if forge || neoforge {
        /*return FMLPaths.CONFIGDIR.get();
        *///?}
    }
}
