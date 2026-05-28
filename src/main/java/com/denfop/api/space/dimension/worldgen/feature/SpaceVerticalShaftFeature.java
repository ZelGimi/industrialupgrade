package com.denfop.api.space.dimension.worldgen.feature;

import com.denfop.api.space.dimension.SpaceDimensionProfile;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SpaceVerticalShaftFeature extends Feature<SpaceBodyFeatureConfig> {

    public SpaceVerticalShaftFeature(final Codec<SpaceBodyFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(final FeaturePlaceContext<SpaceBodyFeatureConfig> context) {
        SpaceDimensionProfile profile = WorldgenSupport.profile(context.config());
        if (!profile.generateVerticalShafts()) {
            return false;
        }
        if (context.random().nextFloat() < 0.95)
            return false;
        WorldGenLevel level = context.level();
        BlockPos top = WorldgenSupport.surfaceCenter(level, context.origin());
        int bottomY = Mth.clamp(top.getY() - 50, level.getMinBuildHeight() + 6, top.getY() - 10);

        for (int y = top.getY(); y >= bottomY; y--) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx * dx + dz * dz > 2) {
                        continue;
                    }
                    BlockPos pos = new BlockPos(top.getX() + dx, y, top.getZ() + dz);
                    if (WorldgenSupport.canReplace(level, pos)) {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            }
        }
        return true;
    }
}
