package com.denfop.api.space.dimension.worldgen.feature;

import com.denfop.api.space.dimension.SpaceDimensionProfile;
import com.denfop.api.space.dimension.SpaceFluidPocket;
import com.denfop.api.space.dimension.worldgen.SpaceTerrainSupportHelper;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.denfop.api.space.dimension.worldgen.feature.SpaceVolcanoPlacementLogic.scheduleFluidAround;

public class SpaceLakeBasinFeature extends Feature<SpaceBodyFeatureConfig> {


    private static final double FLUID_FILL_LIMIT = 0.76D;
    private static final double INNER_BOWL_LIMIT = 1.00D;
    private static final double RIM_LIMIT = 1.18D;

    public SpaceLakeBasinFeature(final Codec<SpaceBodyFeatureConfig> codec) {
        super(codec);
    }

    private static int findStableSurfaceY(
            final WorldGenLevel level,
            final BlockPos origin,
            final int radiusX,
            final int radiusZ
    ) {
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (int dx = -radiusX - 1; dx <= radiusX + 1; dx++) {
            for (int dz = -radiusZ - 1; dz <= radiusZ + 1; dz++) {
                final double dist = ellipseDistance(dx, dz, radiusX, radiusZ);
                if (dist > RIM_LIMIT) {
                    continue;
                }

                final int x = origin.getX() + dx;
                final int z = origin.getZ() + dz;

                if (!hasChunkAt(level, x, z)) {
                    return Integer.MIN_VALUE;
                }

                final int y = SpaceTerrainSupportHelper.findSurfaceY(
                        level,
                        x,
                        z,
                        level.getMaxBuildHeight() - 1
                );

                minY = Math.min(minY, y);
                maxY = Math.max(maxY, y);
            }
        }

        if (minY == Integer.MAX_VALUE) {
            return Integer.MIN_VALUE;
        }

        return maxY - minY > 4 ? Integer.MIN_VALUE : minY;
    }

    private static int findStableUndergroundY(
            final WorldGenLevel level,
            final BlockPos origin,
            final int rawMinY,
            final int rawMaxY,
            final RandomSource random
    ) {
        final int minY = Math.max(level.getMinBuildHeight() + 6, Math.min(rawMinY, rawMaxY));
        final int maxY = Math.min(level.getMaxBuildHeight() - 6, Math.max(rawMinY, rawMaxY));

        if (minY > maxY) {
            return Integer.MIN_VALUE;
        }

        final int baseY = Mth.nextInt(random, minY, maxY);
        final int span = Math.max(baseY - minY, maxY - baseY);

        for (int offset = 0; offset <= span; offset++) {
            final int up = baseY + offset;
            if (up <= maxY && isStableUndergroundCenter(level, origin.atY(up))) {
                return up;
            }

            if (offset > 0) {
                final int down = baseY - offset;
                if (down >= minY && isStableUndergroundCenter(level, origin.atY(down))) {
                    return down;
                }
            }
        }

        return Integer.MIN_VALUE;
    }

    private static boolean isStableUndergroundCenter(
            final WorldGenLevel level,
            final BlockPos center
    ) {
        if (!insideBuildHeight(level, center) || !hasChunkAt(level, center.getX(), center.getZ())) {
            return false;
        }

        final BlockState state = level.getBlockState(center);
        final BlockState below = level.getBlockState(center.below());

        if (state.isAir() || !state.getFluidState().isEmpty()) {
            return false;
        }
        return !below.isAir() && below.getFluidState().isEmpty();
    }

    private static boolean canCarve(final WorldGenLevel level, final BlockPos pos) {
        final BlockState state = level.getBlockState(pos);
        return !state.is(Blocks.BEDROCK) && !state.is(Blocks.BARRIER);
    }

    private static boolean canSeal(final WorldGenLevel level, final BlockPos pos) {
        final BlockState state = level.getBlockState(pos);
        if (state.is(Blocks.BEDROCK) || state.is(Blocks.BARRIER)) {
            return false;
        }

        return state.isAir() || state.canBeReplaced() || !state.getFluidState().isEmpty();
    }

    private static boolean canPlaceLakeFluid(
            final WorldGenLevel level,
            final BlockPos pos
    ) {
        if (!insideBuildHeight(level, pos) || !hasChunkAt(level, pos.getX(), pos.getZ())) {
            return false;
        }

        final BlockState state = level.getBlockState(pos);
        if (state.is(Blocks.BEDROCK) || state.is(Blocks.BARRIER)) {
            return false;
        }

        if (!state.isAir() && !state.canBeReplaced() && state.getFluidState().isEmpty()) {
            return false;
        }

        final BlockPos below = pos.below();
        if (!insideBuildHeight(level, below) || !hasChunkAt(level, below.getX(), below.getZ())) {
            return false;
        }

        final BlockState belowState = level.getBlockState(below);
        if (belowState.isAir()) {
            return false;
        }

        return belowState.getFluidState().isEmpty() || !belowState.isAir();
    }

    private static void safeSetBlock(
            final WorldGenLevel level,
            final BlockPos pos,
            final BlockState state
    ) {
        if (!hasChunkAt(level, pos.getX(), pos.getZ())) {
            return;
        }

        level.setBlock(pos, state, 2);
        level.getChunk(pos).markPosForPostprocessing(pos);
    }

    private static boolean hasChunkAt(
            final WorldGenLevel level,
            final int blockX,
            final int blockZ
    ) {
        return level.hasChunk(
                SectionPos.blockToSectionCoord(blockX),
                SectionPos.blockToSectionCoord(blockZ)
        );
    }

    private static double ellipseDistance(final int dx, final int dz, final int radiusX, final int radiusZ) {
        final double nx = dx / (double) radiusX;
        final double nz = dz / (double) radiusZ;
        return nx * nx + nz * nz;
    }

    private static int localDepth(final double dist, final int depth) {
        return Math.max(1, (int) Math.round((1.0D - Math.min(1.0D, dist)) * depth));
    }

    private static boolean insideBuildHeight(final WorldGenLevel level, final BlockPos pos) {
        return pos.getY() > level.getMinBuildHeight() + 1 && pos.getY() < level.getMaxBuildHeight() - 1;
    }

    private static BlockState nonNull(final BlockState state, final BlockState fallback) {
        return state != null ? state : fallback;
    }

    private static SpaceFluidPocket selectPocket(final SpaceDimensionProfile profile, final RandomSource random) {
        int totalWeight = 0;
        for (SpaceFluidPocket pocket : profile.fluids()) {
            totalWeight += Math.max(1, pocket.weight());
        }

        if (totalWeight <= 0) {
            return profile.fluids().get(random.nextInt(profile.fluids().size()));
        }

        int roll = random.nextInt(totalWeight);
        for (SpaceFluidPocket pocket : profile.fluids()) {
            roll -= Math.max(1, pocket.weight());
            if (roll < 0) {
                return pocket;
            }
        }

        return profile.fluids().get(profile.fluids().size() - 1);
    }

    @Override
    public boolean place(final FeaturePlaceContext<SpaceBodyFeatureConfig> context) {
        final SpaceDimensionProfile profile = WorldgenSupport.profile(context.config());
        if (profile == null || !profile.generateLakes() || profile.fluids().isEmpty()) {
            return false;
        }

        final WorldGenLevel level = context.level();
        final RandomSource random = context.random();

        final SpaceFluidPocket pocket = selectPocket(profile, random);
        if (pocket == null || pocket.state() == null) {
            return false;
        }

        final BlockState fluid = ((FlowingFluid) pocket.fluid()).getSource().defaultFluidState().createLegacyBlock();
        if (fluid.isAir() || fluid.getFluidState().isEmpty()) {
            return false;
        }

        final BlockState top = nonNull(profile.topBlock(), Blocks.STONE.defaultBlockState());
        final BlockState subsurface = nonNull(profile.subsurfaceBlock(), top);

        final int radiusX = Mth.nextInt(random, 5, 9);
        final int radiusZ = Mth.nextInt(random, 5, 9);
        final int depth = Mth.nextInt(random, 3, 6);

        final BlockPos center;
        if (pocket.surfaceLake()) {
            final int stableSurfaceY = findStableSurfaceY(level, context.origin(), radiusX, radiusZ);
            if (stableSurfaceY == Integer.MIN_VALUE) {
                return false;
            }


            center = new BlockPos(context.origin().getX(), stableSurfaceY - 1, context.origin().getZ());
        } else {
            final int undergroundY = findStableUndergroundY(
                    level,
                    context.origin(),
                    pocket.minY(),
                    pocket.maxY(),
                    random
            );
            if (undergroundY == Integer.MIN_VALUE) {
                return false;
            }

            center = context.origin().atY(undergroundY);
        }

        boolean carved = false;


        for (int dx = -radiusX; dx <= radiusX; dx++) {
            for (int dz = -radiusZ; dz <= radiusZ; dz++) {
                final double dist = ellipseDistance(dx, dz, radiusX, radiusZ);
                if (dist > INNER_BOWL_LIMIT) {
                    continue;
                }

                final int localDepth = localDepth(dist, depth);

                for (int dy = 1; dy >= -localDepth; dy--) {
                    final BlockPos pos = center.offset(dx, dy, dz);
                    if (!insideBuildHeight(level, pos) || !hasChunkAt(level, pos.getX(), pos.getZ())) {
                        continue;
                    }

                    if (canCarve(level, pos)) {
                        safeSetBlock(level, pos, Blocks.AIR.defaultBlockState());
                        carved = true;
                    }
                }

                final BlockPos floor = center.offset(dx, -localDepth - 1, dz);
                if (insideBuildHeight(level, floor)
                        && hasChunkAt(level, floor.getX(), floor.getZ())
                        && canSeal(level, floor)) {
                    safeSetBlock(level, floor, subsurface);
                }
            }
        }

        if (!carved) {
            return false;
        }


        for (int dx = -radiusX - 1; dx <= radiusX + 1; dx++) {
            for (int dz = -radiusZ - 1; dz <= radiusZ + 1; dz++) {
                final double dist = ellipseDistance(dx, dz, radiusX, radiusZ);
                if (dist <= FLUID_FILL_LIMIT || dist > RIM_LIMIT) {
                    continue;
                }

                final int x = center.getX() + dx;
                final int z = center.getZ() + dz;
                if (!hasChunkAt(level, x, z)) {
                    continue;
                }

                final int terrainY = SpaceTerrainSupportHelper.findSurfaceY(
                        level,
                        x,
                        z,
                        level.getMaxBuildHeight() - 1
                );

                final int wallBottom = center.getY() - depth - 1;
                final int rimTop = Math.max(center.getY() + 1, terrainY);

                for (int y = wallBottom; y <= rimTop; y++) {
                    final BlockPos pos = new BlockPos(x, y, z);
                    if (!insideBuildHeight(level, pos)) {
                        continue;
                    }

                    final BlockState state = level.getBlockState(pos);
                    if (state.isAir() || state.canBeReplaced() || !state.getFluidState().isEmpty()) {
                        safeSetBlock(level, pos, y >= center.getY() ? top : subsurface);
                    }
                }
            }
        }


        final Set<BlockPos> fluidPositions = new LinkedHashSet<>();

        for (int dx = -radiusX; dx <= radiusX; dx++) {
            for (int dz = -radiusZ; dz <= radiusZ; dz++) {
                final double dist = ellipseDistance(dx, dz, radiusX, radiusZ);
                if (dist > FLUID_FILL_LIMIT) {
                    continue;
                }

                final int localDepth = localDepth(dist, depth);

                for (int dy = 0; dy >= -localDepth; dy--) {
                    final BlockPos pos = center.offset(dx, dy, dz);
                    if (!canPlaceLakeFluid(level, pos)) {
                        continue;
                    }

                    fluidPositions.add(pos.immutable());
                }
            }
        }

        if (fluidPositions.isEmpty()) {
            return false;
        }


        for (BlockPos pos : fluidPositions) {

            level.setBlock(pos, fluid, 2);
            scheduleFluidAround(level, pos, fluid);

        }

        return true;
    }
}