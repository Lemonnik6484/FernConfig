package dev.lemonnik.fern_config;

//? if fabric {
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
//?}

//? if forge {
/*import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
*///?}

//? if neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
*///?}

//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?} else {
/*import net.minecraft.resources.ResourceLocation;
*///?}

//? 1.18.2
//import net.minecraft.network.chat.TextComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.lemonnik.fern_config.impl.TestJSON5Config;
import dev.lemonnik.fern_config.impl.TestTOMLConfig;

import static dev.lemonnik.fern_config.FernConfig.MOD_ID;

//? if forge || neoforge
//@Mod(MOD_ID)

@SuppressWarnings("removal")
public class FernConfig
		//? if fabric
		implements ModInitializer

		{
	public static final String MOD_ID = /*$ mod_id */"fern_config";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final String VERSION = /*$ mod_version */"1.2.0";

	public static final TestJSON5Config JSON5_CONFIG = new TestJSON5Config();
	public static final TestTOMLConfig TOML_CONFIG = new TestTOMLConfig();

	//? if fabric
	@Override public void onInitialize()

	//? if neoforge
	 //public FernConfig(IEventBus modBus)

	//? if forge
	//public FernConfig()

	//? if paper
	/* @Override public void onEnable() */

	{
		LOGGER.info("Starting FernConfig...");

		if (isDev()) {
			JSON5_CONFIG.reload();
			TOML_CONFIG.reload();
		}
	}

	//? >=1.21.11 {
	public static Identifier id(String namespace, String path) {
	//?} else {
	/*public static ResourceLocation id(String namespace, String path) {
	*///?}
		//? if >=1.21.11 {
		return Identifier.tryBuild(namespace, path);
		 //?} else if >1.18.2 {
		/*return ResourceLocation.tryBuild(namespace, path);
		 *///?} else {
		/*return new ResourceLocation(namespace, path);
		*///?}
	}

	private static boolean isDev() {
		//? if fabric
		return FabricLoader.getInstance().isDevelopmentEnvironment();

		//? if forge || (neoforge && <1.21.9)
		//return !FMLLoader.isProduction();

		//? if neoforge && >=1.21.9
		//return !FMLLoader.getCurrent().isProduction();
	}
}