package com.denfop.api.space.dimension.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SpaceSurfaceRiftFeature extends Feature<SpaceFormationFeatureConfig> {

    public SpaceSurfaceRiftFeature(final Codec<SpaceFormationFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(final FeaturePlaceContext<SpaceFormationFeatureConfig> context) {
        final WorldGenLevel level = context.level();
        final RandomSource random = context.random();
        final BlockPos origin = context.origin();
        final SpaceFormationFeatureConfig cfg = context.config();

        if (cfg.kind() != SpaceFormationKind.SURFACE_RIFT) {
            return false;
        }

        final int length = Mth.nextInt(random, cfg.minHeight() + 8, cfg.maxHeight() + 18);
        final int depth = Mth.nextInt(random, cfg.minRadius() + 4, cfg.maxRadius() + 10);
        final int width = Mth.nextInt(random, cfg.minRadius(), cfg.maxRadius());
        float yaw = random.nextFloat() * ((float) Math.PI * 2F);

        for (int i = 0; i < length; i++) {
            final int x = origin.getX() + Mth.floor(Mth.cos(yaw) * i);
            final int z = origin.getZ() + Mth.floor(Mth.sin(yaw) * i);
            final int y = SpaceFeatureUtils.surfaceY(level, x, z) - 1;

            for (int dy = 0; dy < depth; dy++) {
                final int localWidth = Math.max(1, width - dy / 5);
                SpaceFeatureUtils.carveSphere(level, new BlockPos(x, y - dy, z), localWidth, 1, localWidth,
                        net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), s -> !s.isAir());
            }

            if (i % 7 == 0) {
                final int sideY = SpaceFeatureUtils.surfaceY(level, x + width + 1, z);
                level.setBlock(new BlockPos(x + width + 1, sideY, z), cfg.secondary(), 2);
            }

            yaw += (random.nextFloat() - 0.5F) * 0.08F;
        }

        return true;
    }
}