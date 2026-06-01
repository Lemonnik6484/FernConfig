package dev.lemonnik.fern_config;

//? if fabric
import net.fabricmc.api.ModInitializer;

//? if forge
//import net.minecraftforge.fml.common.Mod;

//? if neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
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

//? if forge || neoforge
//@Mod(MOD_ID)

@SuppressWarnings("removal")
public class FernConfig
		//? if fabric
		implements ModInitializer

		{
	public static final String MOD_ID = /*$ mod_id */"fern_config";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final String VERSION = /*$ mod_version */"0.0.1";
	public static final int PATCH = 3;

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
}