package dev.lemonnik.fern_config.types;

import dev.lemonnik.fern_config.CConfig;
import dev.lemonnik.fern_config.CExporter;
import dev.lemonnik.fern_config.utils.CValue;
import dev.lemonnik.fern_config.utils.Mask;
import dev.lemonnik.fern_config.utils.MaskType;
//? if >1.19.2 {
import net.minecraft.core.registries.BuiltInRegistries;
//?} else {
/*import net.minecraft.core.Registry;
*///?}

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class CMask extends CValue<Mask> {
    private final Mask defaultMask;
    private List<String> maskStr;
    private Mask mask;
    private CEnum<MaskType> maskType;

    public CMask(String key, String comment, CEnum<MaskType> maskTypeCEnum, CConfig config, String... maskStr) {
        super(key, comment);

        this.maskStr = List.of(maskStr);
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

    public void setMaskStr(String[] maskStr) {
        this.maskStr = List.of(maskStr);
        this.mask = createMask(maskStr);
    }

    public void setMaskStr(List<String> maskStr) {
        this.maskStr = maskStr;
        this.mask = createMask(maskStr);
    }

    public void addMaskStr(String string) {
        maskStr.add(string);
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
            sb.append("}");
        } else if (format == CExporter.Format.TOML) {
            sb.append("# ").append(comment).append("\n");
            sb.append(key).append(" = {\n");

            for (String line : maskType.getExportStrings(format)) {
                sb.append("    ").append(line).append("\n");
            }

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
        //? if >1.19.2 {
        return new Mask(BuiltInRegistries.BLOCK, maskType.get(), maskStr);
        //?} else {
        /*return new Mask(Registry.BLOCK, maskType, maskStr);
        *///?}
    }
}
