package dev.lemonnik.fern_config.utils;

import dev.lemonnik.fern_config.FernConfig;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import java.util.*;

public class Mask {
    private final MaskType maskType;
    private final Registry<?> registry;
    private final RegistryIndex index;
    private final Set<Identifier> entries;

    public Mask(Registry<?> registry, JSONConfiguration file, String maskKey) {
        this.file = file;
        this.maskType = MaskType.fromString(file.getAsString("type"));
        this.registry = registry;
        this.index = RegistryIndex.getIndex(this.registry);
        this.entries = new HashSet<>();

        for (JsonElement element : file.getAsArray(maskKey)) {
            if (!(element.isJsonPrimitive() && element.getAsJsonPrimitive().isString())) {
                FernConfig.LOGGER.error("Mask element '{}' isn't a string", element);
                return;
            }

            entries.addAll(manageEntry(element.getAsString()));
        }
    }

    public List<Identifier> manageEntry(String entry) {
        String[] split = entry.split(":");

        if (split.length != 2) {
            FernConfig.LOGGER.error("'{}' is not a valid identifier. Correct format is <namespace>:<path>", entry);
            return new ArrayList<>();
        }

        // if *:*
        if (split[0].equals("*") && split[1].equals("*")) {
            return index.getResourceLocations();
        }

        // if <namespace>:<path>
        if (!split[0].equals("*") && !split[1].equals("*")) {
            return List.of(TT20.id(split[0], split[1]));
        }

        // if *:<path>
        if (split[0].equals("*") && !split[1].equals("*")) {
            return index.getPathIndex().getOrDefault(split[1], new ArrayList<>());
        }


        // if <namespace>:*
        if (!split[0].equals("*") && split[1].equals("*")) {
            return index.getNamespaceIndex().getOrDefault(split[0], new ArrayList<>());
        }

        return null;
    }

    public Registry<?> getRegistry() {
        return registry;
    }

    public JSONConfiguration getFile() {
        return file;
    }

    public boolean matches(Identifier identifier) {
        return entries.contains(identifier);
    }

    public boolean isOkay(Identifier identifier) {
        if (maskType == MaskType.WHITELIST) {
            return entries.contains(identifier);
        } else {
            return !entries.contains(identifier);
        }
    }
}
