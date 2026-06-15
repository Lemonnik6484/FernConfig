package dev.lemonnik.fern_config.impl;

import dev.lemonnik.fern_config.CConfig;
import dev.lemonnik.fern_config.CExporter;
import dev.lemonnik.fern_config.FernConfig;
import dev.lemonnik.fern_config.types.*;
import dev.lemonnik.fern_config.utils.CCategory;
import dev.lemonnik.fern_config.utils.MaskType;
import org.jetbrains.annotations.NotNull;

public class TestJSON5Config extends CConfig {
    @Override
    public @NotNull String getFileName() {
        return FernConfig.MOD_ID;
    }

    @Override
    protected @NotNull CExporter.Format getFormat() {
        return CExporter.Format.JSON5;
    }

    private static final CCategory BASIC_THINGS = CCategory.of("basic_category", "Basic values");

    public final CBoolean default_true = register(BASIC_THINGS, new CBoolean("true_by_default", "This boolean's default is 'true'", true));
    public final CBoolean default_false = register(BASIC_THINGS, new CBoolean("false_by_default", "This boolean's default is 'false'", false));

    public final CInteger cool_int = register(BASIC_THINGS, new CInteger("cool_integer", "This is just a number", 67));

    public final CFloat floaty = register(BASIC_THINGS, new CFloat("nice_float", "This is just a number", 123.69F));


    private static final CCategory COMPLEX_THINGS = CCategory.of("complex_category", "Unbasic values", "read carefully");

    public final CEnum<TestEnum> test_enum = register(COMPLEX_THINGS, new CEnum<>("thats_enum", "Only values listed above this comment are acceptable", TestEnum.class, TestEnum.TASTY_ENUM));

    public final CMask block_mask_itself = register(COMPLEX_THINGS, new CMask(
            "block_mask",
            "This is mask itself that requires CEnum-of-MaskType's key to be created and config to access that enum -> ",
            new CEnum<MaskType>(
                    "mask_type",
                    "This is type of mask",
                    MaskType.class,
                    MaskType.WHITELIST
            ),
            this,
            "*:*")
    );
}
