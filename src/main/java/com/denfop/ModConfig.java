package com.denfop;


import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {
    public static final Common COMMON;
    public static final ModConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {


        public final ModConfigSpec.BooleanValue newsystem;

        public final ModConfigSpec.BooleanValue enableEasyMode;
        public final ModConfigSpec.BooleanValue cableEasyMode;
        public final ModConfigSpec.BooleanValue enableExplosion;
        public final ModConfigSpec.BooleanValue enableLosing;

        public final ModConfigSpec.IntValue maxVein;

        public final ModConfigSpec.IntValue gasMaxVein;
        public final ModConfigSpec.IntValue gasChance;
        public final ModConfigSpec.BooleanValue airPollution;
        public final ModConfigSpec.BooleanValue soilPollution;
        public final ModConfigSpec.BooleanValue pressureWork;
        public final ModConfigSpec.BooleanValue damageRadiation;

        public Common(ModConfigSpec.Builder builder) {
            builder.push("General");
            maxVein = builder.comment("Maximum amount of ore in a vein").defineInRange("maxVein", 30000, 1, Integer.MAX_VALUE);
            gasMaxVein = builder.comment("Maximum amount of mb in a gas vein (not natural gas)").defineInRange("gasMaxVein", 200000, 1, Integer.MAX_VALUE);
            gasChance = builder.comment("Chance to get a gas vein (not natural gas)").defineInRange("gasChance", 25, 1, 100);

            builder.pop();

            builder.push("TransformerMode");
            newsystem = builder.comment("Transformer mode enabled").define("TransformerMode", true);
            enableEasyMode = builder.comment("Unchecking the tier").define("enableUnchecking", false);
            cableEasyMode = builder.comment("Unlimiting the conduction of energy in the cable").define("cableUnlimiting", false);
            enableExplosion = builder.comment("Enable explosion from mechanisms if transformer mode is on").define("enableExplosion", true);
            enableLosing = builder.comment("Enable lose energy in cables if transformer mode is on").define("enableLosing", true);
            builder.pop();

            builder.push("pollution");
            airPollution = builder.comment("Air pollution").define("airPollution", true);
            soilPollution = builder.comment("Soil pollution").define("soilPollution", true);
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
