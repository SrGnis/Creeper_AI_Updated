package com.srgnis.creeperaiupdated.config;

import org.apache.commons.lang3.tuple.Pair;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public final class Config
{
    public static final GeneralConfig COMMON;

    private static final ForgeConfigSpec COMMON_SPEC;

    static
    {
        final Pair<GeneralConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(GeneralConfig::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static void register()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
    }

    public static final class GeneralConfig
    {
		public final ForgeConfigSpec.DoubleValue powered_prob;
		public final ForgeConfigSpec.DoubleValue follow_range;
		public final ForgeConfigSpec.BooleanValue can_break_walls;
		public final ForgeConfigSpec.BooleanValue can_leap;
		public final ForgeConfigSpec.BooleanValue fire_explosions;

        GeneralConfig(ForgeConfigSpec.Builder builder)
        {
            builder.push("general");

			powered_prob = builder
					.comment("Probability of creepers spawning powered.")
					.defineInRange("powered_prob", 0.3, 0.0, 1.0);

			follow_range = builder
					.comment("Maximum distance a creeper can see a follow you. (32 = vanilla)")
					.defineInRange("follow_range", 64, 0.0, 2048.0);
			
			can_break_walls = builder
					.comment("Creeper ability to break walls or not")
					.define("can_break_walls", true);
			
			can_leap = builder
					.comment("Creeper ability to leap at targets")
					.define("can_leap", true);
			
			fire_explosions = builder
					.comment("Creeper explosions cause fire")
					.define("fire_explosions", true);

            builder.pop();
        }
    }
}

