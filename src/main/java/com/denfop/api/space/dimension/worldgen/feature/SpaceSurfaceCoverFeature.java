package com.denfop.api.space.dimension.worldgen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public final class SpaceSurfaceCoverFeature extends Feature<NoneFeatureConfiguration> {

    private final int minThickness;
    private final int maxThickness;
    private final boolean cryogenic;

    public SpaceSurfaceCoverFeature(final int minThickness, final int maxThickness, final boolean cryogenic) {
        super(NoneFeatureConfiguration.CODEC);
        this.minThickness = minThickness;
        this.maxThickness = maxThickness;
        this.cryogenic = cryogenic;
    }

    private static boolean isValidBase(
            final WorldGenLevel level,
            final BlockPos pos,
            final BlockState state
    ) {
        if (state.isAir()) {
            return false;
        }
        if (!state.getFluidState().isEmpty()) {
            return false;
        }
        if (state.is(Blocks.BEDROCK)) {
            return false;
        }
        return state.isFaceSturdy(level, pos, Direction.UP);
    }

    private static boolean canReplaceCover(final BlockState state) {
        return state.isAir()
                || state.canBeReplaced()
                || state.is(Blocks.SNOW)
                || state.is(Blocks.SHORT_GRASS)
                || state.is(Blocks.TALL_GRASS)
                || state.is(Blocks.FERN)
                || state.is(Blocks.LARGE_FERN)
                || state.is(Blocks.DEAD_BUSH);
    }

    private static int chunkStart(final int coord) {
        return (coord >> 4) << 4;
    }

    @Override
    public boolean place(final FeaturePlaceContext<NoneFeatureConfiguration> context) {
        final WorldGenLevel level = context.level();
        final RandomSource random = context.random();
        final BlockPos origin = context.origin();

        final int chunkStartX = chunkStart(origin.getX());
        final int chunkStartZ = chunkStart(origin.getZ());

        int placed = 0;
        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                final int x = chunkStartX + localX;
                final int z = chunkStartZ + localZ;

                final int topY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
                if (topY <= level.getMinBuildHeight() + 1 || topY >= level.getMaxBuildHeight() - 2) {
                    continue;
                }

                pos.set(x, topY, z);
                final BlockState baseState = level.getBlockState(pos);
                if (!isValidBase(level, pos, baseState)) {
                    continue;
                }

                final int thickness = Mth.nextInt(random, this.minThickness, this.maxThickness);

                for (int layer = 1; layer <= thickness; layer++) {
                    final int y = topY + layer;
                    if (y >= level.getMaxBuildHeight()) {
                        break;
                    }

                    pos.set(x, y, z);
                    final BlockState existing = level.getBlockState(pos);
                    if (!canReplaceCover(existing)) {
                        break;
                    }

                    level.setBlock(pos, selectCoverState(layer, thickness, random), 2);
                    placed++;
                }
            }
        }

        return placed > 0;
    }

    private BlockState selectCoverState(final int layer, final int thickness, final RandomSource random) {
        if (this.cryogenic) {
            if (layer <= 2) {
                return Blocks.BLUE_ICE.defaultBlockState();
            }
            if (layer >= thickness - 1) {
                return random.nextBoolean()
                        ? Blocks.ICE.defaultBlockState()
                        : Blocks.PACKED_ICE.defaultBlockState();
            }
            return Blocks.PACKED_ICE.defaultBlockState();
        }

        if (layer == thickness) {
            return random.nextBoolean()
                    ? Blocks.SNOW_BLOCK.defaultBlockState()
                    : Blocks.PACKED_ICE.defaultBlockState();
        }
        if (layer == 1) {
            return Blocks.PACKED_ICE.defaultBlockState();
        }
        return Blocks.ICE.defaultBlockState();
    }
}