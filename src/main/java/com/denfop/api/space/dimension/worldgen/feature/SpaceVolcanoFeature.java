package com.denfop.api.space.dimension.worldgen.feature;

import com.denfop.api.space.dimension.SpaceDimensionProfile;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SpaceVolcanoFeature extends Feature<SpaceBodyFeatureConfig> {

    public SpaceVolcanoFeature(final Codec<SpaceBodyFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(final FeaturePlaceContext<SpaceBodyFeatureConfig> context) {
        SpaceDimensionProfile profile = WorldgenSupport.profile(context.config());
        if (!profile.generateVolcanoes()) {
            return false;
        }
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos center = WorldgenSupport.surfaceCenter(level, context.origin());

        int baseRadius = Mth.nextInt(random, 8, 16);
        int height = Mth.nextInt(random, 10, 26);
        final int craterRadius = Math.max(2, baseRadius / 4);
        SpaceVolcanoPlacementLogic.buildVolcanoColumns(
                level,
                center,
                profile.body(),
                baseRadius,
                height,
                craterRadius
        );

        return true;
    }
}
