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
    	public final ForgeConfigSpec.ConfigValue<Integer> maxLayer;
    	public final ForgeConfigSpec.ConfigValue<Integer> minLayer;
		public final ForgeConfigSpec.DoubleValue powered_prob;
		public final ForgeConfigSpec.IntValue follow_range;
		public final ForgeConfigSpec.IntValue breach_range;
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
					.comment("Maximum distance a creeper can see and follow you. (32 = vanilla)")
					.defineInRange("follow_range", 32, 0, 2048);
			
			breach_range = builder
					.comment("Maximum distance a creeper can decide to explode.")
					.defineInRange("breach_range", 30, 0, 2048);
			
			can_break_walls = builder
					.comment("Creeper ability to breach trough walls or not")
					.define("can_break_walls", true);
			
			can_leap = builder
					.comment("Creeper ability to leap at targets")
					.define("can_leap", true);
			
			fire_explosions = builder
					.comment("Creeper explosions cause fire")
					.define("fire_explosions", true);
			
			maxLayer = builder
					.comment("Maximum Y layer where creepers can spawn with the breaching ability.(Sea level = 62)")
					.define("maxLayer", 320);
			
			minLayer = builder
					.comment("Minimum Y layer where creepers can spawn with the breaching ability.(Sea level = 62)")
					.define("minLayer", -64);

            builder.pop();
        }
    }
}

