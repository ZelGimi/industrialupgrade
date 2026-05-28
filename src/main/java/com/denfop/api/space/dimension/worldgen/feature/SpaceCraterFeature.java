package com.denfop.api.space.dimension.worldgen.feature;

import com.denfop.api.space.dimension.SpaceDimensionProfile;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SpaceCraterFeature extends Feature<SpaceBodyFeatureConfig> {

    public SpaceCraterFeature(final Codec<SpaceBodyFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(final FeaturePlaceContext<SpaceBodyFeatureConfig> context) {
        SpaceDimensionProfile profile = WorldgenSupport.profile(context.config());
        if (!profile.generateCraters()) {
            return false;
        }
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos center = WorldgenSupport.surfaceCenter(level, context.origin());
        if (center.getY() <= level.getMinBuildHeight() + 8) {
            return false;
        }

        BlockState rim = profile.rimBlock();
        int radius = Mth.nextInt(random, 7, 18);
        int depth = Math.max(3, radius / 3);

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                double distSq = dx * dx + dz * dz;
                if (distSq > radius * radius) {
                    continue;
                }
                double dist = Math.sqrt(distSq) / radius;
                int x = center.getX() + dx;
                int z = center.getZ() + dz;
                int topY = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE_WG, x, z);
                double bowl = 1.0D - dist * dist;
                int carveDepth = Math.max(1, (int) Math.round(depth * bowl));

                for (int i = 0; i < carveDepth; i++) {
                    BlockPos cut = new BlockPos(x, topY - i - 1, z);
                    if (WorldgenSupport.canReplace(level, cut)) {
                        level.setBlock(cut, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 2);
                    }
                }

                if (dist >= 0.7D && dist <= 1.0D) {
                    int rimHeight = Math.max(1, (int) Math.round((1.0D - Math.abs(0.86D - dist) * 6.0D) * 3.0D));
                    for (int i = 0; i < rimHeight; i++) {
                        BlockPos place = new BlockPos(x, topY + i - 1, z);
                        if (level.getBlockState(place).isAir()) {
                            level.setBlock(place, rim, 2);
                        }
                    }
                }
            }
        }
        return true;
    }
}
