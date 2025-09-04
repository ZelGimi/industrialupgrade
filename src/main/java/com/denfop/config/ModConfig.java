package com.denfop.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {


        public final ForgeConfigSpec.BooleanValue newsystem;

        public final ForgeConfigSpec.BooleanValue enableEasyMode;
        public final ForgeConfigSpec.BooleanValue cableEasyMode;
        public final ForgeConfigSpec.BooleanValue enableExplosion;
        public final ForgeConfigSpec.BooleanValue enableLosing;

        public final ForgeConfigSpec.IntValue maxVein;
        public final ForgeConfigSpec.IntValue gasMaxVein;
        public final ForgeConfigSpec.IntValue gasChance;
        public final ForgeConfigSpec.BooleanValue airPollution;
        public final ForgeConfigSpec.BooleanValue soilPollution;
        public final ForgeConfigSpec.BooleanValue pressureWork;
        public final ForgeConfigSpec.BooleanValue damageRadiation;


        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("Vein");
            maxVein = builder.comment("Maximum amount of ore in a vein").defineInRange("maxVein", 30000, 1, Integer.MAX_VALUE);
            gasMaxVein = builder.comment("Maximum amount of mb in a gas vein (not natural gas)").defineInRange("gasMaxVein", 200000, 1, Integer.MAX_VALUE);
            gasChance = builder.comment("Chance to get a gas vein (not natural gas)").defineInRange("gasChance", 25, 1, 100);
            builder.pop();

            builder.push("pollution");
            airPollution = builder.comment("Air pollution").define("airPollution", true);
            soilPollution = builder.comment("Soil pollution").define("soilPollution", true);
            builder.pop();

            builder.push("TransformerMode");
            newsystem = builder.comment("Transformer mode enabled").define("TransformerMode", true);
            enableEasyMode = builder.comment("Unchecking the tier").define("enableUnchecking", false);
            cableEasyMode = builder.comment("Unlimiting the conduction of energy in the cable").define("cableUnlimiting", false);
            enableExplosion = builder.comment("Enable explosion from mechanisms if transformer mode is on").define("enableExplosion", true);
            enableLosing = builder.comment("Enable lose energy in cables if transformer mode is on").define("enableLosing", true);
            builder.pop();

            builder.push("Pressure Network");
            pressureWork = builder
                    .comment("Remove the pressure level restriction in the pressure system (allows machines to work at any pressure level)")
                    .define("pressureRestriction", true);
            builder.pop();

            builder.push("Radiation");
            damageRadiation = builder
                    .comment("Enable damage from radiation")
                    .define("radiationDamageEnabled", true);
            builder.pop();

        }
    }
}
