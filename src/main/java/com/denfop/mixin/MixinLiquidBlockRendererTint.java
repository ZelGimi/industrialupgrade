package com.denfop.mixin;

import com.denfop.api.pollution.client.PollutionLocalTintCache;
import com.denfop.api.pollution.client.PollutionLocalTintState;
import com.denfop.api.pollution.client.PollutionVisualColors;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LiquidBlockRenderer.class)
public abstract class MixinLiquidBlockRendererTint {

    @Redirect(
            method = "tesselate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/client/extensions/common/IClientFluidTypeExtensions;getTintColor(Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;)I"
            )
    )
    private int iu$applyPollutionWaterTint(
            IClientFluidTypeExtensions extensions,
            FluidState fluidState,
            BlockAndTintGetter getter,
            BlockPos pos
    ) {
        if (pos == null || getter == null) {
            return extensions.getTintColor(fluidState, getter, pos);
        }

        if (!fluidState.is(Fluids.WATER) && !fluidState.is(Fluids.FLOWING_WATER)) {
            return extensions.getTintColor(fluidState, getter, pos);
        }

        BlockPos samplePos = PollutionLocalTintCache.getCellSamplePos(pos);
        int original = extensions.getTintColor(fluidState, getter, samplePos);

        PollutionLocalTintState local = PollutionLocalTintCache.get(samplePos);
        if (!local.isActive()) {
            return original;
        }

        int alpha = (original >>> 24) & 255;
        if (alpha <= 0) {
            alpha = 255;
        }

        int originalRgb = original & 0x00FFFFFF;

        float soilDominance = getSoilDominance(local);
        float radiationDominance = getRadiationDominance(local);

        float strength = Mth.clamp(
                Math.max(
                        local.getBiomeInfluence() * (0.92F + soilDominance * 0.08F - radiationDominance * 0.12F),
                        local.getAirInfluence() * 0.34F
                )
                        + local.getSoilInfluence() * (0.20F + soilDominance * 0.12F)
                        + local.getRadiationInfluence() * 0.18F,
                0.0F,
                1.0F
        );

        int polluted = PollutionVisualColors.applyBiomeDegradation(
                originalRgb,
                local.getWaterTintColor(),
                strength,
                local.getSaturationLoss() * 0.95F
        );

        float muddiness = PollutionVisualColors.smoothstep01(
                Mth.clamp(
                        local.getSoilInfluence() * 0.95F
                                + local.getBiomeInfluence() * 0.32F
                                - local.getRadiationInfluence() * 0.08F,
                        0.0F,
                        1.0F
                )
        );

        if (muddiness > 0.001F) {
            polluted = PollutionVisualColors.desaturate(polluted, muddiness * 0.30F);
            polluted = PollutionVisualColors.lerpColor(
                    polluted,
                    PollutionVisualColors.SOIL_WATER,
                    muddiness * 0.58F
            );
            polluted = PollutionVisualColors.multiply(
                    polluted,
                    Mth.clamp(1.0F - muddiness * 0.24F, 0.60F, 1.0F)
            );
        }

        if (local.getRadiationInfluence() > 0.20F) {
            polluted = PollutionVisualColors.applyRadiationWaterBoost(
                    polluted,
                    local.getRadiationInfluence() * 0.90F
            );
        }

        return (alpha << 24) | (polluted & 0x00FFFFFF);
    }

    private float getSoilDominance(PollutionLocalTintState local) {
        float sum = local.getAirInfluence() + local.getSoilInfluence() + local.getRadiationInfluence();
        if (sum <= 0.0001F) {
            return 0.0F;
        }
        return Mth.clamp(local.getSoilInfluence() / sum, 0.0F, 1.0F);
    }

    private float getRadiationDominance(PollutionLocalTintState local) {
        float sum = local.getAirInfluence() + local.getSoilInfluence() + local.getRadiationInfluence();
        if (sum <= 0.0001F) {
            return 0.0F;
        }
        return Mth.clamp(local.getRadiationInfluence() / sum, 0.0F, 1.0F);
    }
}