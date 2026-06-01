package dev.lemonnik.fern_config.impl;

import dev.lemonnik.fern_config.CConfig;
import dev.lemonnik.fern_config.CExporter;
import dev.lemonnik.fern_config.FernConfig;
import dev.lemonnik.fern_config.types.CBoolean;
import dev.lemonnik.fern_config.types.CEnum;
import dev.lemonnik.fern_config.types.CMask;
import dev.lemonnik.fern_config.utils.CCategory;
import dev.lemonnik.fern_config.utils.MaskType;
import org.jetbrains.annotations.NotNull;

public class TestTOMLConfig extends CConfig {
    @Override
    public @NotNull String getFileName() {
        return FernConfig.MOD_ID + "_toml";
    }

    @Override
    protected @NotNull CExporter.Format getFormat() {
        return CExporter.Format.TOML;
    }


    private static final CCategory BASIC_THINGS = CCategory.of("basic_category", "Basic values");

    public final CBoolean default_true = register(BASIC_THINGS, new CBoolean("true_by_default", "This boolean's default is 'true'", true));
    public final CBoolean default_false = register(BASIC_THINGS, new CBoolean("false_by_default", "This boolean's default is 'false'", false));


    private static final CCategory COMPLEX_THINGS = CCategory.of("complex_category", "Unbasic values", "read carefully");

    public final CEnum<MaskType> block_mask_type = register(COMPLEX_THINGS, new CEnum<MaskType>("mask_type", "This is enum that contains type of the mask below", MaskType.class, MaskType.WHITELIST));
    public final CMask block_mask_itself = register(COMPLEX_THINGS, new CMask("block_mask", "This is mask itself that requires CEnum-of-MaskType's key to be created and config to access that enum -> ", "mask_type", this, "*:*"));
}
