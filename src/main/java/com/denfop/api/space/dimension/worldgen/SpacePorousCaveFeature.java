package com.denfop.api.space.dimension.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.function.Predicate;

public class SpacePorousCaveFeature extends Feature<SpaceCaveFeatureConfig> {

    public SpacePorousCaveFeature(final Codec<SpaceCaveFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(final FeaturePlaceContext<SpaceCaveFeatureConfig> context) {
        final WorldGenLevel level = context.level();
        final RandomSource random = context.random();
        final BlockPos origin = context.origin();
        final SpaceCaveFeatureConfig cfg = context.config();

        if (cfg.kind() != SpaceCaveKind.POROUS) {
            return false;
        }
        if (random.nextFloat() < 0.98)
            return false;
        final Predicate<BlockState> replaceable = state ->
                SpaceFeatureUtils.isReplaceableForCarving(state, cfg.primary(), cfg.secondary(), cfg.tertiary());

        final int clusters = Mth.nextInt(random, cfg.minLength(), cfg.maxLength());
        for (int i = 0; i < clusters; i++) {
            final BlockPos center = origin.offset(
                    random.nextInt(40) - 20,
                    random.nextInt(cfg.maxVertical()) - cfg.maxVertical() / 2,
                    random.nextInt(40) - 20
            );

            final int subPockets = Mth.nextInt(random, 8, 20);
            for (int j = 0; j < subPockets; j++) {
                final BlockPos pocket = center.offset(
                        random.nextInt(14) - 7,
                        random.nextInt(10) - 5,
                        random.nextInt(14) - 7
                );
                final int radius = Mth.nextInt(random, Math.max(1, cfg.minRadius() - 1), cfg.maxRadius());
                SpaceFeatureUtils.carveSphere(level, pocket, radius, radius, radius, Blocks.AIR.defaultBlockState(), replaceable);
            }
        }

        return true;
    }
}