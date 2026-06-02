package dev.lemonnik.fern_config;

import dev.lemonnik.fern_config.types.CBoolean;
import dev.lemonnik.fern_config.types.CEnum;
import dev.lemonnik.fern_config.types.CMask;
import dev.lemonnik.fern_config.utils.CCategory;
import dev.lemonnik.fern_config.utils.CMap;
import dev.lemonnik.fern_config.utils.CValue;

//? if fabric
import net.fabricmc.loader.api.FabricLoader;

//? if neoforge
//import net.neoforged.fml.loading.FMLPaths;

//? if forge
//import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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

    public static CMap load(CMap defaults, String fileName, Format format) {
        Path path = configPath(fileName, format);
        String[] content;
        String currentCategory = "";

        boolean inMask = false;
        String maskKey = "";
        List<String> maskStr = new ArrayList<>();

        try {
            content = Files.readAllLines(path).toArray(new String[0]);
        } catch (Exception e) {
            FernConfig.LOGGER.error("Error reading config file: {}", fileName);
            e.printStackTrace();
            return defaults;
        }

        Pattern categoryPattern = Pattern.compile("");
        Pattern booleanPattern = Pattern.compile("");
        Pattern enumPattern = Pattern.compile("");
        Pattern maskPattern = Pattern.compile("");

        Pattern maskValuePattern = Pattern.compile("\"([^\"]+)\"");

        if (format == Format.JSON5) {
            categoryPattern = Pattern.compile("\"([^\"]*)\":\\s*\\{");
            booleanPattern = Pattern.compile("\"([^\"]+)\":\\s*(true|false)");
            enumPattern = Pattern.compile("\"([^\"]+)\":\\s*\"([A-Z_]+)\"");
            maskPattern = Pattern.compile("\"([^\"]*)\":\\s*\\[");
        } else if (format == Format.TOML) {
            categoryPattern = Pattern.compile("\\[(.+)]");
            booleanPattern = Pattern.compile("(\\w+)\\s*=\\s*(true|false)");
            enumPattern = Pattern.compile("(\\w+)\\s*=\\s*\"([A-Z_]+)\"");
            maskPattern = Pattern.compile("(\\w+)\\s*=\\s*\\[");
        }

        for (String line : content) {
            Matcher categoryMatcher = categoryPattern.matcher(line);
            if (categoryMatcher.find()) {
                currentCategory = categoryMatcher.group(1);
                continue;
            }

            Matcher booleanMatcher = booleanPattern.matcher(line);
            if (booleanMatcher.find()) {
                String key = booleanMatcher.group(1);
                boolean value = Boolean.parseBoolean(booleanMatcher.group(2));

                for (CCategory cCategory : defaults.map().keySet()) {
                    if (cCategory.id().equals(currentCategory)) {
                        for (CValue<?> cValue : defaults.map().get(cCategory)) {
                            if (cValue.key.equals(key) && cValue instanceof CBoolean cBoolean) {
                                cBoolean.set(value);
                                break;
                            }
                        }
                        break;
                    }
                }
                continue;
            }

            Matcher enumMatcher = enumPattern.matcher(line);
            if (enumMatcher.find()) {
                String key = enumMatcher.group(1);
                String value = enumMatcher.group(2);

                for (CCategory cCategory : defaults.map().keySet()) {
                    if (cCategory.id().equals(currentCategory)) {
                        for (CValue<?> cValue : defaults.map().get(cCategory)) {
                            if (cValue.key.equals(key) && cValue instanceof CEnum<?> cEnum) {
                                cEnum.setEnumStr(value);
                                break;
                            }
                        }
                        break;
                    }
                }
                continue;
            }

            Matcher maskMatcher = maskPattern.matcher(line);
            if (maskMatcher.find()) {
                maskKey = maskMatcher.group(1);
                inMask = true;
                continue;
            }
            Matcher maskValueMatcher = maskValuePattern.matcher(line);
            if (inMask && maskValueMatcher.find()) {
                String value = maskValueMatcher.group(1);
                maskStr.add(value);
                continue;
            }
            if (inMask && line.startsWith("]")) {
                inMask = false;

                for (CCategory cCategory : defaults.map().keySet()) {
                    if (cCategory.id().equals(currentCategory)) {
                        for (CValue<?> cValue : defaults.map().get(cCategory)) {
                            if (cValue.key.equals(maskKey) && cValue instanceof CMask cMask) {
                                cMask.setMaskStr(maskStr);
                                break;
                            }
                        }
                        break;
                    }
                }
                continue;
            }
        }

        return defaults;
    }

    public static boolean save(CMap config, String fileName, Format format) {
        Path path = configPath(fileName, format);
        StringBuilder sb = new StringBuilder();
        String tab = "   ";
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
