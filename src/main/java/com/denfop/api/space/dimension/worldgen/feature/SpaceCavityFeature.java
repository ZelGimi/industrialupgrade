package com.denfop.api.space.dimension.worldgen.feature;

import com.denfop.api.space.dimension.SpaceDimensionProfile;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SpaceCavityFeature extends Feature<SpaceBodyFeatureConfig> {

    public SpaceCavityFeature(final Codec<SpaceBodyFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(final FeaturePlaceContext<SpaceBodyFeatureConfig> context) {
        SpaceDimensionProfile profile = WorldgenSupport.profile(context.config());
        if (!profile.generateCavities()) {
            return false;
        }

        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        if (random.nextFloat() < 0.95)
            return false;
        BlockPos center = context.origin().atY(Mth.nextInt(random, -48, 64));
        int rx = Mth.nextInt(random, 5, 12);
        int ry = Mth.nextInt(random, 3, 8);
        int rz = Mth.nextInt(random, 5, 12);

        for (int dx = -rx; dx <= rx; dx++) {
            for (int dy = -ry; dy <= ry; dy++) {
                for (int dz = -rz; dz <= rz; dz++) {
                    double nx = dx / (double) rx;
                    double ny = dy / (double) ry;
                    double nz = dz / (double) rz;
                    if (nx * nx + ny * ny + nz * nz > 1.0D) {
                        continue;
                    }
                    BlockPos pos = center.offset(dx, dy, dz);
                    if (WorldgenSupport.canReplace(level, pos)) {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            }
        }
        return true;
    }
}
