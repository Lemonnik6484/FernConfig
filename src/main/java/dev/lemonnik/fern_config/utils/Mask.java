package dev.lemonnik.fern_config.utils;

import dev.lemonnik.fern_config.FernConfig;
import net.minecraft.core.Registry;
//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?} else {
/*import net.minecraft.resources.ResourceLocation;
*///?}

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Mask {
    private final MaskType maskType;
    private final Registry<?> registry;
    private final RegistryIndex index;
    //? if >=1.21.11 {
    private final Set<Identifier> entries;
    //?} else {
    /*private final Set<ResourceLocation> entries;
    *///?}

    public Mask(Registry<?> registry, MaskType maskType, List<String> entries) {
        this.registry = registry;
        this.maskType = maskType;
        this.index = RegistryIndex.getIndex(registry);
        this.entries = new LinkedHashSet<>();

        for (String entry : entries) {
            this.entries.addAll(manageEntry(entry));
        }
    }

    //? if >=1.21.11 {
    public List<Identifier> manageEntry(String entry) {
    //?} else {
    /*public List<ResourceLocation> manageEntry(String entry) {
    *///?}
        String[] split = entry.split(":", -1);

        if (split.length != 2 || split[0].isEmpty() || split[1].isEmpty()) {
            FernConfig.LOGGER.error("'{}' is not a valid identifier. Correct format is <namespace>:<path>", entry);
            return List.of();
        }

        if (split[0].equals("*") && split[1].equals("*")) {
            return index.getResourceLocations();
        }

        if (split[0].equals("*")) {
            return index.getPathIndex().getOrDefault(split[1], List.of());
        }

        if (split[1].equals("*")) {
            return index.getNamespaceIndex().getOrDefault(split[0], List.of());
        }

        var identifier = FernConfig.id(split[0], split[1]);
        if (identifier == null) {
            FernConfig.LOGGER.error("'{}' is not a valid identifier. Correct format is <namespace>:<path>", entry);
            return List.of();
        }

        return List.of(identifier);
    }

    public Registry<?> getRegistry() {
        return registry;
    }

    public MaskType getMaskType() {
        return maskType;
    }

    //? if >=1.21.11 {
    public Set<Identifier> getEntries() {
    //?} else {
    /*public Set<ResourceLocation> getEntries() {
    *///?}
        return Collections.unmodifiableSet(entries);
    }

    //? if >=1.21.11 {
    public boolean matches(Identifier identifier) {
    //?} else {
    /*public boolean matches(ResourceLocation identifier) {
    *///?}
        return entries.contains(identifier);
    }

    //? if >=1.21.11 {
    public boolean isOkay(Identifier identifier) {
    //?} else {
    /*public boolean isOkay(ResourceLocation identifier) {
    *///?}
        if (maskType == MaskType.WHITELIST) {
            return entries.contains(identifier);
        } else {
            return !entries.contains(identifier);
        }
    }
}
