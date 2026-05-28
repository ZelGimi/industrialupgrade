package com.denfop.api.pollution.client;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public final class PollutionEnvironmentBlockColors {

    private static final ThreadLocal<BlockPos.MutableBlockPos> MUTABLE_SAMPLE_POS =
            ThreadLocal.withInitial(BlockPos.MutableBlockPos::new);
    public static final BlockColor WATER_PLANT_HANDLER = (state, getter, pos, tintIndex) -> {
        if (getter == null || pos == null) {
            return 0x3F76E4;
        }

        return getWaterPlantTintFromWorld(getter, pos);
    };
    private static final BlockColor GRASS_HANDLER = (state, getter, pos, tintIndex) -> {
        if (getter == null || pos == null) {
            return GrassColor.get(0.5D, 1.0D);
        }

        long cellKey = PollutionLocalTintCache.getCellKey(pos);
        int sampleX = PollutionLocalTintCache.getCellSampleX(cellKey);
        int sampleZ = PollutionLocalTintCache.getCellSampleZ(cellKey);

        BlockPos.MutableBlockPos samplePos = MUTABLE_SAMPLE_POS.get();
        samplePos.set(sampleX, pos.getY(), sampleZ);

        int base = BiomeColors.getAverageGrassColor(getter, samplePos);
        PollutionLocalTintState local = PollutionLocalTintCache.get(pos);

        if (!local.isActive()) {
            return base;
        }

        return PollutionBlockColorCache.getOrCompute(
                cellKey,
                base,
                PollutionBlockColorCache.TintType.GRASS,
                () -> {
                    float radiationDominance = getRadiationDominance(local);
                    float soilDominance = getSoilDominance(local);

                    float strength = Mth.clamp(
                            local.getBiomeInfluence() * (0.78F + soilDominance * 0.08F - radiationDominance * 0.10F)
                                    + local.getSoilInfluence() * (0.18F + soilDominance * 0.08F)
                                    + local.getAirInfluence() * 0.05F
                                    + local.getRadiationInfluence() * 0.18F,
                            0.0F,
                            1.0F
                    );

                    int polluted = PollutionVisualColors.applyBiomeDegradation(
                            base,
                            local.getGrassTintColor(),
                            strength,
                            local.getSaturationLoss() * (1.00F - radiationDominance * 0.18F)
                    );

                    polluted = applyContextDarkening(
                            polluted,
                            local,
                            0.12F,
                            0.05F,
                            0.70F
                    );

                    if (local.getRadiationInfluence() > 0.22F) {
                        polluted = PollutionVisualColors.applyRadiationGreenBoost(
                                polluted,
                                local.getRadiationInfluence() * 0.95F
                        );
                    }

                    return polluted;
                }
        );
    };
    private static final BlockColor FOLIAGE_HANDLER = (state, getter, pos, tintIndex) -> {
        if (getter == null || pos == null) {
            return FoliageColor.getDefaultColor();
        }

        long cellKey = PollutionLocalTintCache.getCellKey(pos);
        int sampleX = PollutionLocalTintCache.getCellSampleX(cellKey);
        int sampleZ = PollutionLocalTintCache.getCellSampleZ(cellKey);

        BlockPos.MutableBlockPos samplePos = MUTABLE_SAMPLE_POS.get();
        samplePos.set(sampleX, pos.getY(), sampleZ);

        int base = BiomeColors.getAverageFoliageColor(getter, samplePos);
        PollutionLocalTintState local = PollutionLocalTintCache.get(pos);

        if (!local.isActive()) {
            return base;
        }

        return PollutionBlockColorCache.getOrCompute(
                cellKey,
                base,
                PollutionBlockColorCache.TintType.FOLIAGE,
                () -> {
                    float radiationDominance = getRadiationDominance(local);
                    float soilDominance = getSoilDominance(local);

                    float strength = Mth.clamp(
                            local.getBiomeInfluence() * (0.72F + soilDominance * 0.08F - radiationDominance * 0.08F)
                                    + local.getSoilInfluence() * (0.18F + soilDominance * 0.06F)
                                    + local.getAirInfluence() * 0.06F
                                    + local.getRadiationInfluence() * 0.22F,
                            0.0F,
                            1.0F
                    );

                    int polluted = PollutionVisualColors.applyBiomeDegradation(
                            base,
                            local.getFoliageTintColor(),
                            strength,
                            local.getSaturationLoss() * (1.00F - radiationDominance * 0.16F)
                    );

                    polluted = applyContextDarkening(
                            polluted,
                            local,
                            0.11F,
                            0.05F,
                            0.72F
                    );

                    if (local.getRadiationInfluence() > 0.24F) {
                        polluted = PollutionVisualColors.applyRadiationGreenBoost(
                                polluted,
                                local.getRadiationInfluence() * 1.00F
                        );
                    }

                    return polluted;
                }
        );
    };

    private PollutionEnvironmentBlockColors() {
    }

    public static void register(RegisterColorHandlersEvent.Block event) {
        event.register(
                GRASS_HANDLER,
                Blocks.GRASS_BLOCK,
                Blocks.SHORT_GRASS,
                Blocks.TALL_GRASS,
                Blocks.FERN,
                Blocks.LARGE_FERN
        );

        event.register(
                FOLIAGE_HANDLER,
                Blocks.OAK_LEAVES,
                Blocks.SPRUCE_LEAVES,
                Blocks.BIRCH_LEAVES,
                Blocks.JUNGLE_LEAVES,
                Blocks.ACACIA_LEAVES,
                Blocks.DARK_OAK_LEAVES,
                Blocks.MANGROVE_LEAVES,
                Blocks.AZALEA_LEAVES,
                Blocks.FLOWERING_AZALEA_LEAVES,
                Blocks.VINE
        );

        event.register(
                WATER_PLANT_HANDLER,
                Blocks.SEAGRASS,
                Blocks.TALL_SEAGRASS,
                Blocks.KELP,
                Blocks.KELP_PLANT
        );
    }

    public static int getWaterPlantTintFromWorld(BlockAndTintGetter getter, BlockPos pos) {
        long cellKey = PollutionLocalTintCache.getCellKey(pos);
        int sampleX = PollutionLocalTintCache.getCellSampleX(cellKey);
        int sampleZ = PollutionLocalTintCache.getCellSampleZ(cellKey);

        BlockPos.MutableBlockPos samplePos = MUTABLE_SAMPLE_POS.get();
        samplePos.set(sampleX, pos.getY(), sampleZ);

        int base = BiomeColors.getAverageWaterColor(getter, samplePos);
        return applyWaterPlantTint(cellKey, base, pos);
    }

    public static int applyWaterPlantTint(long cellKey, int baseColor, BlockPos pos) {
        PollutionLocalTintState local = PollutionLocalTintCache.get(pos);
        if (!local.isActive()) {
            return baseColor;
        }

        return PollutionBlockColorCache.getOrCompute(
                cellKey,
                baseColor,
                PollutionBlockColorCache.TintType.WATER_PLANT,
                () -> computeWaterTint(baseColor, local)
        );
    }

    private static int computeWaterTint(int base, PollutionLocalTintState local) {
        float radiationDominance = getRadiationDominance(local);
        float soilDominance = getSoilDominance(local);

        float strength = Mth.clamp(
                Math.max(
                        local.getBiomeInfluence() * (0.88F + soilDominance * 0.05F - radiationDominance * 0.14F),
                        local.getAirInfluence() * 0.40F
                )
                        + local.getSoilInfluence() * (0.12F + soilDominance * 0.05F)
                        + local.getRadiationInfluence() * 0.22F,
                0.0F,
                1.0F
        );

        int polluted = PollutionVisualColors.applyBiomeDegradation(
                base,
                local.getWaterTintColor(),
                strength,
                local.getSaturationLoss() * 0.82F
        );

        polluted = applyContextDarkening(
                polluted,
                local,
                0.08F,
                0.04F,
                0.76F
        );

        if (local.getRadiationInfluence() > 0.22F) {
            polluted = PollutionVisualColors.applyRadiationWaterBoost(
                    polluted,
                    local.getRadiationInfluence() * 0.90F
            );
        }

        return polluted;
    }

    private static float getRadiationDominance(PollutionLocalTintState local) {
        float sum = local.getAirInfluence() + local.getSoilInfluence() + local.getRadiationInfluence();
        if (sum <= 0.0001F) {
            return 0.0F;
        }
        return Mth.clamp(local.getRadiationInfluence() / sum, 0.0F, 1.0F);
    }

    private static float getSoilDominance(PollutionLocalTintState local) {
        float sum = local.getAirInfluence() + local.getSoilInfluence() + local.getRadiationInfluence();
        if (sum <= 0.0001F) {
            return 0.0F;
        }
        return Mth.clamp(local.getSoilInfluence() / sum, 0.0F, 1.0F);
    }

    private static int applyContextDarkening(
            int color,
            PollutionLocalTintState local,
            float soilFactor,
            float biomeFactor,
            float minBrightness
    ) {
        float radiationDominance = getRadiationDominance(local);

        float darkness = Mth.clamp(
                local.getSoilInfluence() * soilFactor
                        + local.getBiomeInfluence() * biomeFactor,
                0.0F,
                0.24F
        );

        darkness *= (1.0F - radiationDominance * 0.55F);

        float brightness = Mth.clamp(1.0F - darkness, minBrightness, 1.0F);
        return PollutionVisualColors.multiply(color, brightness);
    }
}