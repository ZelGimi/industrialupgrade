package com.denfop.api.space.dimension.worldgen.feature;

import com.denfop.api.space.dimension.SpaceDimensionProfile;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SpaceLavaTubeFeature extends Feature<SpaceBodyFeatureConfig> {

    public SpaceLavaTubeFeature(final Codec<SpaceBodyFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(final FeaturePlaceContext<SpaceBodyFeatureConfig> context) {
        SpaceDimensionProfile profile = WorldgenSupport.profile(context.config());
        if (!profile.generateLavaTubes()) {
            return false;
        }

        return true;
    }

    private void carveBubble(final WorldGenLevel level, final BlockPos center, final int radius, final net.minecraft.world.level.block.state.BlockState fluid) {
        for (int ox = -radius; ox <= radius; ox++) {
            for (int oy = -1; oy <= 1; oy++) {
                for (int oz = -radius; oz <= radius; oz++) {
                    if (ox * ox + oy * oy + oz * oz > radius * radius) {
                        continue;
                    }
                    BlockPos pos = center.offset(ox, oy, oz);
                    if (WorldgenSupport.canReplace(level, pos)) {
                        level.setBlock(pos, oy <= -1 ? fluid : Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            }
        }
    }
}
