package com.tobzi.keepthemsmall;

import com.tobzi.keepthemsmall.command.ReloadConfig;
import com.tobzi.keepthemsmall.config.ModConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeepThemSmall implements ModInitializer {
	public static final String MOD_ID = "keep-them-small";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        ModConfig.load();
		LOGGER.info("Initializing Keep Them Small");


        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ReloadConfig.register(dispatcher);
        });
	}
}