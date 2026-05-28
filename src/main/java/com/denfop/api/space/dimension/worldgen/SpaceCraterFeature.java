package com.denfop.api.space.dimension.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SpaceCraterFeature extends Feature<SpaceFormationFeatureConfig> {

    public SpaceCraterFeature(final Codec<SpaceFormationFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(final FeaturePlaceContext<SpaceFormationFeatureConfig> context) {
        final WorldGenLevel level = context.level();
        final RandomSource random = context.random();
        final BlockPos origin = context.origin();
        final SpaceFormationFeatureConfig cfg = context.config();

        if (cfg.kind() != SpaceFormationKind.CRATER) {
            return false;
        }

        final int surfaceY = SpaceFeatureUtils.surfaceY(level, origin.getX(), origin.getZ());
        final BlockPos center = new BlockPos(origin.getX(), surfaceY - 1, origin.getZ());
        final int radius = Mth.nextInt(random, cfg.maxRadius() + 3, cfg.maxRadius() + 9);
        final int depth = Math.max(4, radius / 2);

        SpaceFeatureUtils.carveSphere(level, center, radius, depth, radius, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), state -> !state.isAir());
        SpaceFeatureUtils.paintShell(level, center, radius + 1, depth + 1, radius + 1, cfg.secondary(), state -> true);

        final int rimRadius = radius + 3;
        for (int dx = -rimRadius; dx <= rimRadius; dx++) {
            for (int dz = -rimRadius; dz <= rimRadius; dz++) {
                final int distSq = dx * dx + dz * dz;
                if (distSq < radius * radius || distSq > rimRadius * rimRadius) {
                    continue;
                }
                final int y = SpaceFeatureUtils.surfaceY(level, center.getX() + dx, center.getZ() + dz);
                level.setBlock(new BlockPos(center.getX() + dx, y, center.getZ() + dz), cfg.primary(), 2);
            }
        }

        return true;
    }
}