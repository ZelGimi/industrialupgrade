package com.denfop.api.space.dimension.worldgen.feature;

import com.denfop.api.space.dimension.*;
import com.denfop.api.space.dimension.worldgen.SpaceTerrainSupportHelper;
import com.denfop.blocks.FluidName;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class SpaceVolcanoPlacementLogic {

    private SpaceVolcanoPlacementLogic() {
    }

    public static void buildVolcanoColumns(
            final WorldGenLevel level,
            final BlockPos center,
            final SpaceBodyRef body,
            final int radius,
            final int height,
            final int craterRadius
    ) {
        buildVolcanoColumns(
                level,
                center,
                body,
                radius,
                height,
                craterRadius,
                createDeterministicRandom(center, body)
        );
    }

    public static void buildVolcanoColumns(
            final WorldGenLevel level,
            final BlockPos center,
            final SpaceBodyRef body,
            final int radius,
            final int height,
            final int craterRadius,
            final RandomSource random
    ) {
        final SpaceDimensionProfile profile = SpaceBodyProfiles.byBody(body);
        final SpaceGenerationMode mode = profile.generationMode();
        final SpaceBodyDefinition definition = SpaceBodyDefinitionRegistry.getOrBuild(body, mode);

        final BlockState stone = nonNull(
                definition.getLayer(SpaceBodyDefinitionRegistry.STONE),
                Blocks.STONE.defaultBlockState()
        );
        final BlockState top = nonNull(
                definition.getLayer(SpaceBodyDefinitionRegistry.TOP),
                stone
        );
        final BlockState rim = nonNull(
                definition.getLayer(SpaceBodyDefinitionRegistry.RIM),
                stone
        );
        final BlockState cobble = nonNull(
                definition.getLayer(SpaceBodyDefinitionRegistry.COBBLE),
                stone
        );

        final BlockState fallbackFluid = toSourceFluidBlock(nonNull(
                definition.getLayer(SpaceBodyDefinitionRegistry.FLUID),
                FluidName.fluidpahoehoe_lava.getInstance().get().getSource().defaultFluidState().createLegacyBlock()
        ));
        final BlockState fluid = resolveRandomVolcanoFluid(profile, random, fallbackFluid);

        final int centerSurfaceY = SpaceTerrainSupportHelper.findSurfaceY(
                level,
                center.getX(),
                center.getZ(),
                center.getY() + height + 24
        );

        final int centerTopY = centerSurfaceY + computeConeHeight(0.0D, radius, height);
        final int craterDepth = Math.max(2, Math.min(6, craterRadius + 1));
        final int craterFloorY = Math.max(centerSurfaceY + 2, centerTopY - craterDepth);

        final Map<Long, Integer> lowestByColumn = SpaceTerrainSupportHelper.createColumnTracker();
        final Set<BlockPos> craterFluidPositions = new LinkedHashSet<>();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                final double dist = Math.sqrt(dx * dx + dz * dz);
                if (dist > radius) {
                    continue;
                }

                final int x = center.getX() + dx;
                final int z = center.getZ() + dz;

                final int localSurfaceY = SpaceTerrainSupportHelper.findSurfaceY(
                        level,
                        x,
                        z,
                        centerSurfaceY + height + 16
                );

                final int coneHeight = computeConeHeight(dist, radius, height);
                final int topY = centerSurfaceY + coneHeight;

                if (topY <= localSurfaceY) {
                    continue;
                }

                final double innerCraterLimit = Math.max(1.0D, craterRadius - 0.90D);
                final double fluidCraterLimit = Math.max(0.5D, craterRadius - 1.20D);
                final boolean innerCrater = dist < innerCraterLimit;
                final boolean craterFluidColumn = dist <= fluidCraterLimit;
                final boolean rimColumn = dist >= Math.max(0.0D, craterRadius - 1.35D)
                        && dist <= craterRadius + 0.75D;

                final int solidTopY = innerCrater ? craterFloorY : topY;

                if (solidTopY > localSurfaceY) {
                    for (int y = localSurfaceY; y <= solidTopY; y++) {
                        final BlockPos pos = new BlockPos(x, y, z);
                        final BlockState state = pickVolcanoState(
                                y,
                                localSurfaceY,
                                solidTopY,
                                dist,
                                radius,
                                rimColumn,
                                stone,
                                top,
                                rim,
                                cobble
                        );

                        SpaceTerrainSupportHelper.placeTrackedSolid(level, pos, state, lowestByColumn);
                    }
                }

                if (innerCrater) {
                    for (int y = Math.max(localSurfaceY + 1, craterFloorY + 1); y <= topY; y++) {
                        final BlockPos pos = new BlockPos(x, y, z);
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }

                if (rimColumn) {
                    final int rimBaseY = Math.max(localSurfaceY + 1, topY - 2);
                    for (int y = rimBaseY; y <= topY; y++) {
                        final BlockPos pos = new BlockPos(x, y, z);
                        SpaceTerrainSupportHelper.placeTrackedSolid(level, pos, rim, lowestByColumn);
                    }
                }


                if (craterFluidColumn && craterFloorY > localSurfaceY) {
                    final int fillDepth = Math.max(2, Math.min(4, craterDepth));

                    for (int fy = 0; fy < fillDepth; fy++) {
                        final int yy = craterFloorY + fy;
                        if (yy >= topY) {
                            break;
                        }

                        final BlockPos fluidPos = new BlockPos(x, yy, z);
                        if (canPlaceFluid(level, fluidPos)) {
                            craterFluidPositions.add(fluidPos.immutable());
                        }
                    }
                }
            }
        }


        SpaceTerrainSupportHelper.stitchUnsupportedColumnsDown(
                level,
                lowestByColumn,
                new BlockPos(center.getX(), centerSurfaceY, center.getZ()),
                craterRadius,
                stone,
                cobble
        );

        for (BlockPos pos : craterFluidPositions) {
            placeFluidNoUpdates(level, pos, fluid);
        }

        for (BlockPos pos : craterFluidPositions) {
            scheduleFluidAround(level, pos, fluid);
        }
    }

    private static BlockState resolveRandomVolcanoFluid(
            final SpaceDimensionProfile profile,
            final RandomSource random,
            final BlockState fallback
    ) {
        final List<SpaceFluidPocket> fluids = profile.fluids();
        if (fluids == null || fluids.isEmpty()) {
            return toSourceFluidBlock(fallback);
        }

        final SpaceFluidPocket picked = pickWeightedPocket(fluids, random);
        if (picked == null || picked.state() == null || picked.state().isAir()) {
            return toSourceFluidBlock(fallback);
        }

        final BlockState resolved = toSourceFluidBlock(picked.state());
        return resolved.isAir() ? toSourceFluidBlock(fallback) : resolved;
    }

    private static SpaceFluidPocket pickWeightedPocket(
            final List<SpaceFluidPocket> fluids,
            final RandomSource random
    ) {
        int totalWeight = 0;
        for (SpaceFluidPocket pocket : fluids) {
            totalWeight += Math.max(1, pocket.weight());
        }

        if (totalWeight <= 0) {
            return fluids.get(random.nextInt(fluids.size()));
        }

        int roll = random.nextInt(totalWeight);
        for (SpaceFluidPocket pocket : fluids) {
            roll -= Math.max(1, pocket.weight());
            if (roll < 0) {
                return pocket;
            }
        }

        return fluids.get(fluids.size() - 1);
    }

    private static void placeFluidNoUpdates(
            final LevelAccessor level,
            final BlockPos pos,
            final BlockState fluidState
    ) {
        if (fluidState == null || fluidState.isAir() || fluidState.getFluidState().isEmpty()) {
            return;
        }

        level.setBlock(pos, fluidState, 2);
    }

    static void scheduleFluidAround(
            final LevelAccessor level,
            final BlockPos pos,
            final BlockState fluidState
    ) {
        if (fluidState == null || fluidState.isAir() || fluidState.getFluidState().isEmpty()) {
            return;
        }

        final Fluid fluid = fluidState.getFluidState().getType();
        if (fluid == Fluids.EMPTY) {
            return;
        }

        level.scheduleTick(pos, fluid, 1);
        level.scheduleTick(pos.below(), fluid, 1);
        level.scheduleTick(pos.above(), fluid, 1);

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            level.scheduleTick(pos.relative(direction), fluid, 1);
        }
    }

    private static boolean canPlaceFluid(
            final WorldGenLevel level,
            final BlockPos pos
    ) {
        final BlockState state = level.getBlockState(pos);

        if (state.is(Blocks.BEDROCK) || state.is(Blocks.BARRIER)) {
            return false;
        }

        if (state.isAir()) {
            return !level.getBlockState(pos.below()).isAir();
        }

        if (state.canBeReplaced()) {
            return !level.getBlockState(pos.below()).isAir();
        }

        return !state.getFluidState().isEmpty();
    }

    private static BlockState toSourceFluidBlock(final BlockState state) {
        if (state == null || state.isAir()) {
            return Blocks.AIR.defaultBlockState();
        }

        final Fluid fluid = state.getFluidState().getType();
        if (fluid == Fluids.EMPTY) {
            return state;
        }

        if (fluid instanceof FlowingFluid flowingFluid) {
            return flowingFluid.getSource().defaultFluidState().createLegacyBlock();
        }

        return fluid.defaultFluidState().createLegacyBlock();
    }

    private static BlockState pickVolcanoState(
            final int y,
            final int localSurfaceY,
            final int topY,
            final double dist,
            final int radius,
            final boolean rimColumn,
            final BlockState stone,
            final BlockState top,
            final BlockState rim,
            final BlockState cobble
    ) {
        if (y == topY) {
            return rimColumn ? rim : top;
        }

        if (y >= topY - 2) {
            return rimColumn ? rim : stone;
        }

        if (dist >= radius - 1.5D) {
            return cobble;
        }

        if (y <= localSurfaceY + 1) {
            return cobble;
        }

        return stone;
    }

    private static int computeConeHeight(
            final double dist,
            final int radius,
            final int maxHeight
    ) {
        final double normalized = 1.0D - (dist / Math.max(1.0D, radius));
        final double curved = Math.pow(normalized, 1.35D);
        return Mth.floor(curved * maxHeight);
    }

    private static RandomSource createDeterministicRandom(
            final BlockPos center,
            final SpaceBodyRef body
    ) {
        long seed = center.asLong();
        seed ^= ((long) body.name().hashCode() << 32);
        seed ^= 0x9E3779B97F4A7C15L;
        return RandomSource.create(seed);
    }

    private static BlockState nonNull(final BlockState state, final BlockState fallback) {
        return state != null ? state : fallback;
    }
}