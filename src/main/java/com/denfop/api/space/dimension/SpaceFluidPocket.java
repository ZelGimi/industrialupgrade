package com.denfop.api.space.dimension;

import com.denfop.api.space.dimension.worldgen.SpaceFluidDistributionType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

public record SpaceFluidPocket(
        BlockState state,
        int weight,
        int minY,
        int maxY,
        SpaceVentType ventType,
        boolean surfaceLake, Fluid fluid
) {
    public String name() {
        final ResourceLocation key = BuiltInRegistries.FLUID.getKey(this.state.getFluidState().getType());
        return key != null ? key.getPath() : "unknown_fluid";
    }

    public String fluidId() {
        final ResourceLocation key = BuiltInRegistries.FLUID.getKey(this.state.getFluidState().getType());
        return key != null ? key.toString() : "minecraft:empty";
    }

    public SpaceFluidDistributionType distributionType() {
        return switch (this.ventType) {
            case LAVA -> SpaceFluidDistributionType.LAVA_SYSTEM;
            case ACID -> SpaceFluidDistributionType.ACID;
            case CRYO -> SpaceFluidDistributionType.CRYO;
            case GAS -> SpaceFluidDistributionType.GAS;
            case STEAM -> this.surfaceLake
                    ? SpaceFluidDistributionType.SURFACE_LAKE
                    : SpaceFluidDistributionType.POCKET;
        };
    }

    public int radiusMin() {
        if (this.surfaceLake) {
            return 3;
        }
        return Math.max(1, Math.min(4, this.weight / 20));
    }

    public int radiusMax() {
        return Math.max(radiusMin(), Math.min(24, Math.max(4, this.weight / 3)));
    }

    public float baseChance() {
        return Math.max(0.15F, Math.min(1.0F, this.weight / 96.0F));
    }

    public boolean caveOnly() {
        return !this.surfaceLake;
    }

    public boolean surfaceAllowed() {
        return this.surfaceLake;
    }

    public boolean requireHotContext() {
        return this.ventType == SpaceVentType.LAVA || this.ventType == SpaceVentType.ACID;
    }

    public boolean requireColdContext() {
        return this.ventType == SpaceVentType.CRYO;
    }
}
