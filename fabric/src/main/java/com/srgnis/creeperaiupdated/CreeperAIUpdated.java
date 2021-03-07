package com.srgnis.creeperaiupdated;


import com.srgnis.creeperaiupdated.config.ConfigInstance;
import com.srgnis.creeperaiupdated.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreeperAIUpdated implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "creeperaiupdated";
    public static ConfigInstance config;
    public static CreeperAIUpdated INSTANCE;

    @Override
    public void onInitialize() {
        ModConfig.init();
        config = ModConfig.INSTANCE;
        config.breachingRadius *= config.breachingRadius;

        INSTANCE = this;
    }


}
