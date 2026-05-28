package com.denfop.api.pollution.client;

import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.pollution.component.ChunkLevel;
import com.denfop.api.pollution.radiation.Radiation;
import com.denfop.api.pollution.radiation.RadiationSystem;
import com.denfop.config.ModConfig;
import com.denfop.potion.IUPotion;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public final class PollutionVisualCalculator {

    private static final float AIR_RADIUS = 2.35F;
    private static final float SOIL_RADIUS = 2.15F;
    private static final float RADIATION_RADIUS = 2.55F;

    private PollutionVisualCalculator() {
    }

    public static PollutionVisualData calculate(LocalPlayer player) {
        if (player == null || player.level() == null || player.level().dimension() != Level.OVERWORLD) {
            return PollutionVisualData.NONE;
        }

        ChunkPos center = new ChunkPos(player.blockPosition());

        float air = sampleAir(center);
        float soil = sampleSoil(center);
        float radiation = sampleRadiation(center);
        float negativeEffects = sampleNegativeEffects(player);

        if (negativeEffects > 0.0F) {
            radiation = Mth.clamp(Math.max(radiation, negativeEffects * 0.55F), 0.0F, 1.0F);
            air = Mth.clamp(Math.max(air, negativeEffects * 0.25F), 0.0F, 1.0F);
        }

        float overall = Mth.clamp(
                Math.max(Math.max(air, radiation), soil * 0.90F)
                        + air * 0.18F
                        + soil * 0.12F
                        + radiation * 0.22F,
                0.0F,
                1.0F
        );

        if (overall < 0.01F) {
            return PollutionVisualData.NONE;
        }

        float screen = Mth.clamp(overall * 0.42F + radiation * 0.36F + air * 0.16F + negativeEffects * 0.60F, 0.0F, 1.0F);
        float sky = Mth.clamp(Math.max(air * 0.92F, radiation * 0.74F + soil * 0.10F), 0.0F, 1.0F);
        float fog = Mth.clamp(Math.max(air * 0.96F, radiation * 0.72F) + soil * 0.18F, 0.0F, 1.0F);
        float biome = Mth.clamp(soil * 0.94F + air * 0.30F + radiation * 0.26F, 0.0F, 1.0F);
        float saturation = Mth.clamp(overall * 0.28F + radiation * 0.18F + soil * 0.10F, 0.0F, 1.0F);
        float horizon = Mth.clamp(air * 0.84F + radiation * 0.44F + soil * 0.24F, 0.0F, 1.0F);
        float cloudVeil = Mth.clamp(air * 0.76F + radiation * 0.48F + soil * 0.16F, 0.0F, 1.0F);
        float shimmer = Mth.clamp(radiation * 0.88F + air * 0.10F, 0.0F, 1.0F);

        float wa = air * 1.00F;
        float ws = soil * 0.95F;
        float wr = radiation * 1.10F;

        float weightSum = wa + ws + wr;
        if (weightSum <= 0.0001F) {
            wa = 1.0F;
            ws = 0.0F;
            wr = 0.0F;
            weightSum = 1.0F;
        }

        wa /= weightSum;
        ws /= weightSum;
        wr /= weightSum;

        PollutionVisualType dominantType = determineDominantType(air, soil, radiation);

        int skyColor = PollutionVisualColors.weightedColor(
                PollutionVisualColors.AIR_SKY,
                PollutionVisualColors.SOIL_SKY,
                PollutionVisualColors.RADIATION_SKY,
                wa,
                ws,
                wr
        );

        int fogColor = PollutionVisualColors.weightedColor(
                PollutionVisualColors.AIR_FOG,
                PollutionVisualColors.SOIL_FOG,
                PollutionVisualColors.RADIATION_FOG,
                wa,
                ws,
                wr
        );

        int overlayColor = PollutionVisualColors.weightedColor(
                PollutionVisualColors.AIR_OVERLAY,
                PollutionVisualColors.SOIL_OVERLAY,
                PollutionVisualColors.RADIATION_OVERLAY,
                wa,
                ws,
                wr
        );

        int grassTintColor = PollutionVisualColors.weightedColor(
                PollutionVisualColors.AIR_GRASS,
                PollutionVisualColors.SOIL_GRASS,
                PollutionVisualColors.RADIATION_GRASS,
                wa,
                ws,
                wr
        );

        int foliageTintColor = PollutionVisualColors.weightedColor(
                PollutionVisualColors.AIR_FOLIAGE,
                PollutionVisualColors.SOIL_FOLIAGE,
                PollutionVisualColors.RADIATION_FOLIAGE,
                wa,
                ws,
                wr
        );

        int waterTintColor = PollutionVisualColors.weightedColor(
                PollutionVisualColors.AIR_WATER,
                PollutionVisualColors.SOIL_WATER,
                PollutionVisualColors.RADIATION_WATER,
                wa,
                ws,
                wr
        );

        float fogNearFactor = Mth.lerp(fog, 1.0F, 0.78F);
        float fogFarFactor = Mth.lerp(fog, 1.0F, 0.46F);

        if (air > 0.55F) {
            fogFarFactor = Mth.lerp(air, fogFarFactor, 0.38F);
        }

        if (player.isUnderWater()) {
            float waterSeverity = Mth.clamp(Math.max(soil, air * 0.72F), 0.0F, 1.0F);
            fogNearFactor = Mth.lerp(waterSeverity, fogNearFactor, 0.62F);
            fogFarFactor = Mth.lerp(waterSeverity, fogFarFactor, 0.24F);
        }

        return new PollutionVisualData(
                dominantType,
                overall,
                air,
                soil,
                radiation,
                sky,
                fog,
                screen,
                biome,
                saturation,
                horizon,
                cloudVeil,
                shimmer,
                fogNearFactor,
                fogFarFactor,
                skyColor,
                fogColor,
                overlayColor,
                grassTintColor,
                foliageTintColor,
                waterTintColor
        );
    }

    private static PollutionVisualType determineDominantType(float air, float soil, float radiation) {
        float max = Math.max(Math.max(air, soil), radiation);
        if (max < 0.02F) {
            return PollutionVisualType.NONE;
        }

        float second;
        if (max == air) {
            second = Math.max(soil, radiation);
        } else if (max == soil) {
            second = Math.max(air, radiation);
        } else {
            second = Math.max(air, soil);
        }

        if (second > max * 0.82F) {
            return PollutionVisualType.MIXED;
        }

        if (max == air) {
            return PollutionVisualType.AIR;
        }
        if (max == soil) {
            return PollutionVisualType.SOIL;
        }
        return PollutionVisualType.RADIATION;
    }

    private static float sampleAir(ChunkPos center) {
        if (!ModConfig.COMMON.airPollution.get()) {
            return 0.0F;
        }

        PollutionManager manager = PollutionManager.pollutionManager;
        if (manager == null) {
            return 0.0F;
        }

        return sampleChunkField(center, AIR_RADIUS, pos -> pollutionSeverity(manager.getChunkLevelAir(pos)));
    }

    private static float sampleSoil(ChunkPos center) {
        if (!ModConfig.COMMON.soilPollution.get()) {
            return 0.0F;
        }

        PollutionManager manager = PollutionManager.pollutionManager;
        if (manager == null) {
            return 0.0F;
        }

        return sampleChunkField(center, SOIL_RADIUS, pos -> pollutionSeverity(manager.getChunkLevelSoil(pos)));
    }

    private static float sampleRadiation(ChunkPos center) {
        if (!ModConfig.COMMON.radiationChunksEnabled.get()) {
            return 0.0F;
        }

        RadiationSystem system = RadiationSystem.rad_system;
        if (system == null) {
            return 0.0F;
        }

        return sampleChunkField(center, RADIATION_RADIUS, pos -> radiationSeverity(system.getMap().get(pos)));
    }

    private static float sampleChunkField(ChunkPos center, float radius, ChunkSeverityProvider provider) {
        int maxOffset = Mth.ceil(radius);

        float maxInfluence = 0.0F;
        float weightedSum = 0.0F;
        float totalWeight = 0.0F;

        for (int dx = -maxOffset; dx <= maxOffset; dx++) {
            for (int dz = -maxOffset; dz <= maxOffset; dz++) {
                float distance = Mth.sqrt((float) (dx * dx + dz * dz));
                if (distance > radius) {
                    continue;
                }

                ChunkPos pos = new ChunkPos(center.x + dx, center.z + dz);
                float severity = provider.getSeverity(pos);
                if (severity <= 0.0F) {
                    continue;
                }

                float rawFalloff = 1.0F - (distance / (radius + 0.65F));
                float falloff = PollutionVisualColors.smoothstep01(rawFalloff);
                float proximityWeight = 0.30F + falloff * 0.70F;

                if (dx == 0 && dz == 0) {
                    proximityWeight *= 1.15F;
                }

                weightedSum += severity * proximityWeight;
                totalWeight += proximityWeight;

                float localPeak = severity * (0.55F + falloff * 0.45F);
                if (dx == 0 && dz == 0) {
                    localPeak *= 1.10F;
                }

                maxInfluence = Math.max(maxInfluence, localPeak);
            }
        }

        float average = totalWeight > 0.0F ? (weightedSum / totalWeight) : 0.0F;
        return Mth.clamp(Math.max(maxInfluence, average), 0.0F, 1.0F);
    }

    private static float pollutionSeverity(ChunkLevel chunkLevel) {
        if (chunkLevel == null) {
            return 0.0F;
        }

        float level = chunkLevel.getLevelPollution().ordinal();
        float withinLevel = Mth.clamp((float) (chunkLevel.getPollution() / 125.0D), 0.0F, 1.0F);

        if (level <= 0.0F && withinLevel < 0.05F) {
            return 0.0F;
        }

        return Mth.clamp(level * 0.21F + withinLevel * 0.11F, 0.0F, 1.0F);
    }

    private static float radiationSeverity(Radiation radiation) {
        if (radiation == null) {
            return 0.0F;
        }

        float level = radiation.getLevel().ordinal();
        float withinLevel = Mth.clamp((float) (radiation.getRadiation() / 1000.0D), 0.0F, 1.0F);

        if (level <= 0.0F && withinLevel < 0.03F) {
            return 0.0F;
        }

        return Mth.clamp(level * 0.24F + withinLevel * 0.16F, 0.0F, 1.0F);
    }

    private static float sampleNegativeEffects(LocalPlayer player) {
        float bonus = 0.0F;

        if (player.hasEffect(MobEffects.BLINDNESS)) {
            bonus += 0.18F;
        }
        if (player.hasEffect(MobEffects.CONFUSION)) {
            bonus += 0.10F;
        }
        if (player.hasEffect(MobEffects.WITHER)) {
            bonus += 0.12F;
        }
        if (player.hasEffect(MobEffects.POISON)) {
            bonus += 0.06F;
        }
        if (player.hasEffect(IUPotion.poison)) {
            bonus += 0.22F;
        }
        if (player.hasEffect(IUPotion.rad)) {
            bonus += 0.18F;
        }

        double storedRadiation = player.getPersistentData().getDouble("radiation");
        if (storedRadiation > 0.0D) {
            bonus += Mth.clamp((float) (storedRadiation / 60.0D), 0.0F, 0.25F);
        }

        return Mth.clamp(bonus, 0.0F, 1.0F);
    }

    @FunctionalInterface
    private interface ChunkSeverityProvider {
        float getSeverity(ChunkPos pos);
    }
}