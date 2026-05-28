package com.denfop.api.space.dimension.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SpaceDebrisFieldFeature extends Feature<SpaceFormationFeatureConfig> {

    public SpaceDebrisFieldFeature(final Codec<SpaceFormationFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(final FeaturePlaceContext<SpaceFormationFeatureConfig> context) {
        final WorldGenLevel level = context.level();
        final RandomSource random = context.random();
        final BlockPos origin = context.origin();
        final SpaceFormationFeatureConfig cfg = context.config();

        if (cfg.kind() != SpaceFormationKind.DEBRIS_FIELD) {
            return false;
        }

        final int patches = Mth.nextInt(random, 8, 20);
        for (int i = 0; i < patches; i++) {
            final int x = origin.getX() + random.nextInt(28) - 14;
            final int z = origin.getZ() + random.nextInt(28) - 14;
            final int y = SpaceFeatureUtils.surfaceY(level, x, z);
            final int radius = Mth.nextInt(random, cfg.minRadius(), cfg.maxRadius());

            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (dx * dx + dz * dz > radius * radius) {
                        continue;
                    }
                    final BlockPos pos = new BlockPos(x + dx, y + random.nextInt(2), z + dz);
                    if (level.getBlockState(pos).isAir() || level.getBlockState(pos).canBeReplaced()) {
                        level.setBlock(pos, random.nextBoolean() ? cfg.primary() : cfg.secondary(), 2);
                    }
                }
            }
        }
        return true;
    }
}