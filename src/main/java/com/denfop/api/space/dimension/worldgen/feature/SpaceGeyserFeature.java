package com.denfop.api.space.dimension.worldgen.feature;

import com.denfop.api.space.dimension.SpaceDimensionProfile;
import com.denfop.api.space.dimension.SpaceFluidPocket;
import com.denfop.api.space.dimension.worldgen.SpaceWorldgenContent;
import com.denfop.api.space.dimension.worldgen.block.SpaceGeyserBlock;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.denfop.api.space.dimension.worldgen.feature.SpaceVolcanoPlacementLogic.scheduleFluidAround;

public class SpaceGeyserFeature extends Feature<SpaceBodyFeatureConfig> {

    public SpaceGeyserFeature(final Codec<SpaceBodyFeatureConfig> codec) {
        super(codec);
    }

    private static SpaceFluidPocket selectPocket(
            final SpaceDimensionProfile profile,
            final RandomSource random
    ) {
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

    @Override
    public boolean place(final FeaturePlaceContext<SpaceBodyFeatureConfig> context) {
        final SpaceDimensionProfile profile = WorldgenSupport.profile(context.config());
        if (profile == null || !profile.generateGeysers() || profile.fluids().isEmpty()) {
            return false;
        }

        final WorldGenLevel level = context.level();
        final RandomSource random = context.random();
        final SpaceFluidPocket pocket = selectPocket(profile, random);
        if (pocket == null || pocket.state() == null) {
            return false;
        }

        final BlockState fluidState = ((FlowingFluid) pocket.fluid()).getSource().defaultFluidState().createLegacyBlock();
        if (fluidState.isAir() || fluidState.getFluidState().isEmpty()) {
            return false;
        }

        final BlockPos centerGround = this.findGroundSurface(level, context.origin());
        if (centerGround == null) {
            return false;
        }

        final int extraVentCount = 3 + random.nextInt(2);
        final int fieldRadius = 2 + random.nextInt(2);
        final int mainShaftRadius = 2 + random.nextInt(2);
        final int mainShaftDepth = 4 + random.nextInt(2);
        final int reservoirRadiusXZ = mainShaftRadius + 1 + random.nextInt(2);
        final int reservoirRadiusY = 2 + random.nextInt(2);

        boolean placed = false;
        final Set<BlockPos> fluidPositions = new LinkedHashSet<>();


        placed |= this.collectReservoir(
                level,
                centerGround.below(mainShaftDepth + 4),
                reservoirRadiusXZ,
                reservoirRadiusY,
                random,
                fluidPositions
        );


        placed |= this.placeVentAndClosedShaft(
                level,
                centerGround,
                pocket,
                random,
                mainShaftRadius,
                mainShaftDepth,
                1,
                true,
                fluidPositions
        );


        final Set<BlockPos> usedPositions = new HashSet<>();
        usedPositions.add(centerGround);

        for (int i = 0; i < extraVentCount; i++) {
            BlockPos candidate = null;

            for (int attempt = 0; attempt < 24; attempt++) {
                final double angle = random.nextDouble() * Math.PI * 2.0D;
                final double distance = 3.0D + random.nextDouble() * fieldRadius;

                final int x = centerGround.getX() + Mth.floor(Math.cos(angle) * distance);
                final int z = centerGround.getZ() + Mth.floor(Math.sin(angle) * distance);

                final BlockPos ground = this.findGroundSurface(level, x, z);
                if (ground == null) {
                    continue;
                }

                if (!this.isFarEnough(usedPositions, ground, 3)) {
                    continue;
                }

                candidate = ground;
                break;
            }

            if (candidate == null) {
                continue;
            }

            usedPositions.add(candidate);

            final int shaftRadius = 1 + random.nextInt(2);
            final int shaftDepth = 4 + random.nextInt(3);
            final int ventBlockRadius = 1;

            placed |= this.placeVentAndClosedShaft(
                    level,
                    candidate,
                    pocket,
                    random,
                    shaftRadius,
                    shaftDepth,
                    ventBlockRadius,
                    false,
                    fluidPositions
            );
        }

        if (!placed || fluidPositions.isEmpty()) {
            return placed;
        }


        for (BlockPos pos : fluidPositions) {
            level.setBlock(pos, fluidState, 2);
            scheduleFluidAround(level, pos, fluidState);

        }

        return true;
    }

    private BlockPos findGroundSurface(final WorldGenLevel level, final BlockPos origin) {
        return this.findGroundSurface(level, origin.getX(), origin.getZ());
    }

    private BlockPos findGroundSurface(final WorldGenLevel level, final int x, final int z) {
        if (!hasChunkAt(level, x, z)) {
            return null;
        }

        final int height = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z);
        final int y = height - 1;

        if (!this.isInsideBuildHeight(level, y)) {
            return null;
        }

        final BlockPos groundPos = new BlockPos(x, y, z);
        final BlockState groundState = level.getBlockState(groundPos);

        if (groundState.isAir()) {
            return null;
        }
        if (!level.getFluidState(groundPos).isEmpty()) {
            return null;
        }

        return groundPos;
    }

    private boolean placeVentAndClosedShaft(
            final WorldGenLevel level,
            final BlockPos groundCenter,
            final SpaceFluidPocket pocket,
            final RandomSource random,
            final int shaftRadius,
            final int shaftDepth,
            final int ventBlockRadius,
            final boolean mainVent,
            final Set<BlockPos> fluidPositions
    ) {
        boolean placed = false;


        for (int dx = -ventBlockRadius; dx <= ventBlockRadius; dx++) {
            for (int dz = -ventBlockRadius; dz <= ventBlockRadius; dz++) {
                final double distSq = dx * dx + dz * dz;
                final double maxDist = ventBlockRadius * ventBlockRadius + random.nextDouble() * 0.75D;
                if (distSq > maxDist) {
                    continue;
                }

                final int localX = groundCenter.getX() + dx;
                final int localZ = groundCenter.getZ() + dz;
                final BlockPos localGround = this.findGroundSurface(level, localX, localZ);
                if (localGround == null) {
                    continue;
                }

                final BlockPos ventPos = localGround.above();
                if (!this.canPlaceVentAt(level, ventPos)) {
                    continue;
                }

                safeSetBlock(
                        level,
                        ventPos,
                        SpaceWorldgenContent.SPACE_GEYSER.get().defaultBlockState()
                                .setValue(SpaceGeyserBlock.VENT_TYPE, pocket.ventType())
                                .setValue(SpaceGeyserBlock.ACTIVE, Boolean.TRUE)
                );
                placed = true;
            }
        }


        for (int depth = 2; depth <= shaftDepth + 1; depth++) {
            int radius = shaftRadius;
            if (mainVent && depth > (shaftDepth / 2) + 1) {
                radius += 1;
            }

            placed |= this.collectDisk(
                    level,
                    groundCenter.below(depth),
                    radius,
                    random,
                    fluidPositions
            );
        }

        return placed;
    }

    private boolean collectReservoir(
            final WorldGenLevel level,
            final BlockPos center,
            final int radiusXZ,
            final int radiusY,
            final RandomSource random,
            final Set<BlockPos> fluidPositions
    ) {
        boolean placed = false;

        final int radiusX = radiusXZ + random.nextInt(2);
        final int radiusZ = radiusXZ + random.nextInt(2);

        for (int dx = -radiusX; dx <= radiusX; dx++) {
            for (int dy = -radiusY; dy <= radiusY; dy++) {
                for (int dz = -radiusZ; dz <= radiusZ; dz++) {
                    final double nx = dx / (double) radiusX;
                    final double ny = dy / (double) radiusY;
                    final double nz = dz / (double) radiusZ;
                    final double distance = nx * nx + ny * ny + nz * nz;

                    if (distance > 1.0D + random.nextDouble() * 0.18D) {
                        continue;
                    }

                    final BlockPos pos = center.offset(dx, dy, dz);
                    if (!this.canOverwrite(level, pos)) {
                        continue;
                    }

                    fluidPositions.add(pos.immutable());
                    placed = true;
                }
            }
        }

        return placed;
    }

    private boolean collectDisk(
            final WorldGenLevel level,
            final BlockPos center,
            final int radius,
            final RandomSource random,
            final Set<BlockPos> fluidPositions
    ) {
        boolean placed = false;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                final double distSq = dx * dx + dz * dz;
                if (distSq > radius * radius + random.nextDouble() * 1.25D) {
                    continue;
                }

                final BlockPos pos = center.offset(dx, 0, dz);
                if (!this.canOverwrite(level, pos)) {
                    continue;
                }

                fluidPositions.add(pos.immutable());
                placed = true;
            }
        }

        return placed;
    }

    private boolean canPlaceVentAt(final WorldGenLevel level, final BlockPos pos) {
        if (!this.isInsideBuildHeight(level, pos.getY())) {
            return false;
        }
        if (!hasChunkAt(level, pos.getX(), pos.getZ())) {
            return false;
        }

        final BlockState state = level.getBlockState(pos);
        return state.isAir() || WorldgenSupport.canReplace(level, pos);
    }

    private boolean canOverwrite(final WorldGenLevel level, final BlockPos pos) {
        if (!this.isInsideBuildHeight(level, pos.getY())) {
            return false;
        }
        if (!hasChunkAt(level, pos.getX(), pos.getZ())) {
            return false;
        }

        final BlockState state = level.getBlockState(pos);

        if (state.is(Blocks.BEDROCK) || state.is(Blocks.BARRIER)) {
            return false;
        }
        if (state.is(SpaceWorldgenContent.SPACE_GEYSER.get())) {
            return false;
        }

        return true;
    }

    private boolean isInsideBuildHeight(final WorldGenLevel level, final int y) {
        return y > level.getMinBuildHeight() + 1 && y < level.getMaxBuildHeight() - 2;
    }

    private boolean isFarEnough(final Set<BlockPos> used, final BlockPos candidate, final int minDistance) {
        final int minDistanceSq = minDistance * minDistance;

        for (final BlockPos pos : used) {
            final int dx = pos.getX() - candidate.getX();
            final int dz = pos.getZ() - candidate.getZ();
            if (dx * dx + dz * dz < minDistanceSq) {
                return false;
            }
        }

        return true;
    }

}