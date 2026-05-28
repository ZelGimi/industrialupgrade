package com.denfop.api.space.dimension;

import com.denfop.api.space.dimension.worldgen.SpaceOreDistributionType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public record SpaceOreEntry(
        BlockState state,
        int weight,
        int veinSize,
        int minY,
        int maxY
) {
    public String name() {
        final ResourceLocation key = BuiltInRegistries.BLOCK.getKey(this.state.getBlock());
        return key != null ? key.getPath() : "unknown_ore";
    }

    public String oreBlockId() {
        final ResourceLocation key = BuiltInRegistries.BLOCK.getKey(this.state.getBlock());
        return key != null ? key.toString() : "minecraft:air";
    }

    public List<String> replaceableBlockIds() {
        return List.of();
    }

    public SpaceOreDistributionType distributionType() {
        if (this.minY <= -56) {
            return SpaceOreDistributionType.DEEP;
        }
        if ((this.maxY - this.minY) <= 12) {
            return SpaceOreDistributionType.LAYERED;
        }
        if (this.weight <= 10) {
            return SpaceOreDistributionType.SCATTERED;
        }
        if (this.veinSize >= 10) {
            return SpaceOreDistributionType.VEIN;
        }
        return SpaceOreDistributionType.CLUSTER;
    }

    public int size() {
        return this.veinSize;
    }

    public int pathLength() {
        return Math.max(8, this.veinSize * 2);
    }

    public int horizontalRadius() {
        return Math.max(3, this.veinSize);
    }

    public int verticalRadius() {
        return Math.max(2, this.veinSize / 2);
    }

    public float baseChance() {
        return Math.max(0.15F, Math.min(1.0F, this.weight / 64.0F));
    }

    public float depthBonus() {
        return this.minY <= -40 ? 0.35F : 0.0F;
    }

    public boolean requireNearAir() {
        return this.weight <= 10;
    }

    public boolean requireNearFluid() {
        return false;
    }

    public boolean requireNearLava() {
        return this.minY <= -40;
    }

    public boolean caveOnly() {
        return false;
    }
}
