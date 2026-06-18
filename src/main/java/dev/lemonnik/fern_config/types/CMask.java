package dev.lemonnik.fern_config.types;

import dev.lemonnik.fern_config.CExporter;
import dev.lemonnik.fern_config.utils.CValue;
import dev.lemonnik.fern_config.utils.Mask;
import dev.lemonnik.fern_config.utils.MaskType;
import net.minecraft.core.Registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("deprecation")
public class CMask extends CValue<Mask> {
    private final Mask defaultMask;
    private List<String> maskStr;
    private Mask mask;
    private final Registry<?> registry;
    private final CEnum<MaskType> maskType;

    public CMask(String key, String comment, Registry<?> registry, CEnum<MaskType> maskTypeCEnum, String... maskStr) {
        super(key, comment);

        this.maskStr = List.of(maskStr);
        this.registry = registry;
        this.maskType = maskTypeCEnum;
        this.defaultMask = createMask(maskStr);
        this.mask = createMask(maskStr);
    }

    @Override
    public Mask get() {
        return mask;
    }

    public List<String> getMaskStr() {
        return maskStr;
    }

    public void setMaskStr(List<String> maskStr) {
        this.maskStr = maskStr;
        this.mask = createMask(maskStr);
    }

    public void setMaskType(MaskType maskType) {
        this.maskType.set(maskType);
        this.mask = createMask(maskStr);
    }

    @Override
    public void set(Mask mask) {
        this.mask = mask;
    }

    @Override
    public boolean isDefault() {
        return mask.getEntries().equals(defaultMask.getEntries());
    }

    @Override
    public String[] getExportStrings(CExporter.Format format) {
        StringBuilder sb = new StringBuilder();

        if (format == CExporter.Format.JSON5) {
            sb.append("// ").append(comment).append("\n");
            sb.append("\"").append(key).append("\": {\n");

            for (String line : maskType.getExportStrings(format)) {
                sb.append("    ").append(line).append("\n");
            }

            sb.append("    ").append("\"values\"").append(": [\n");
            for (String mask : getMaskStr()) {
                sb.append("        \"").append(mask).append("\",\n");
            }
            sb.append("    ").append("],\n");
            sb.append("},");
        } else if (format == CExporter.Format.TOML) {
            sb.append("# ").append(comment).append("\n");
            sb.append(key).append(" = {\n");

            for (String line : maskType.getExportStrings(format)) {
                sb.append("    ").append(line).append("\n");
            }
            sb.delete(sb.length() - 1, sb.length()).append(",\n");

            sb.append("    ").append("values").append(" = [\n");
            for (String mask : getMaskStr()) {
                sb.append("        \"").append(mask).append("\",\n");
            }
            sb.append("    ").append("],\n");
            sb.append("}");
        }

        return sb.toString().split("\n");
    }

    @Override
    public String prefix() {
        return "M";
    }

    private Mask createMask(String[] maskStr) {
        return createMask(List.of(maskStr));
    }

    private Mask createMask(List<String> maskStr) {
        return new Mask(registry, maskType.get(), maskStr);
    }
}
