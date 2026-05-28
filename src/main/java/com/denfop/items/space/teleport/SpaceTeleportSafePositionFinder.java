package com.denfop.items.space.teleport;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.Heightmap;

public final class SpaceTeleportSafePositionFinder {

    private SpaceTeleportSafePositionFinder() {
    }

    public static BlockPos findNearestSafe(final ServerLevel level, final BlockPos requested, final int radius) {
        BlockPos center = clamp(level, requested);

        BlockPos direct = adjustAt(level, center);
        if (direct != null) {
            return direct;
        }

        for (int ring = 1; ring <= radius; ring++) {
            int minX = center.getX() - ring;
            int maxX = center.getX() + ring;
            int minZ = center.getZ() - ring;
            int maxZ = center.getZ() + ring;

            for (int x = minX; x <= maxX; x++) {
                BlockPos north = adjustAt(level, new BlockPos(x, center.getY(), minZ));
                if (north != null) {
                    return north;
                }

                BlockPos south = adjustAt(level, new BlockPos(x, center.getY(), maxZ));
                if (south != null) {
                    return south;
                }
            }

            for (int z = minZ + 1; z <= maxZ - 1; z++) {
                BlockPos west = adjustAt(level, new BlockPos(minX, center.getY(), z));
                if (west != null) {
                    return west;
                }

                BlockPos east = adjustAt(level, new BlockPos(maxX, center.getY(), z));
                if (east != null) {
                    return east;
                }
            }
        }

        BlockPos spawn = adjustAt(level, level.getSharedSpawnPos());
        if (spawn != null) {
            return spawn;
        }

        int fallbackY = Mth.clamp(level.getSharedSpawnPos().getY(), level.getMinBuildHeight() + 1, level.getMaxBuildHeight() - 2);
        return new BlockPos(level.getSharedSpawnPos().getX(), fallbackY, level.getSharedSpawnPos().getZ());
    }

    private static BlockPos clamp(final LevelHeightAccessor level, final BlockPos pos) {
        int y = Mth.clamp(pos.getY(), level.getMinBuildHeight() + 1, level.getMaxBuildHeight() - 2);
        return new BlockPos(pos.getX(), y, pos.getZ());
    }

    private static BlockPos adjustAt(final ServerLevel level, final BlockPos base) {
        WorldBorder border = level.getWorldBorder();

        if (!border.isWithinBounds(base)) {
            return null;
        }

        int top = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, base.getX(), base.getZ());
        int min = level.getMinBuildHeight() + 1;
        int max = level.getMaxBuildHeight() - 2;
        int[] checks = new int[]{
                Mth.clamp(top, min, max),
                Mth.clamp(top + 1, min, max),
                Mth.clamp(top - 1, min, max),
                Mth.clamp(base.getY(), min, max),
                Mth.clamp(base.getY() + 1, min, max),
                Mth.clamp(base.getY() - 1, min, max),
                Mth.clamp(base.getY() + 2, min, max),
                Mth.clamp(base.getY() - 2, min, max)
        };

        for (int y : checks) {
            BlockPos candidate = new BlockPos(base.getX(), y, base.getZ());
            if (isSafe(level, candidate)) {
                return candidate;
            }
        }

        return null;
    }

    private static boolean isSafe(final ServerLevel level, final BlockPos feet) {
        WorldBorder border = level.getWorldBorder();
        if (!border.isWithinBounds(feet) || !border.isWithinBounds(feet.above())) {
            return false;
        }

        if (feet.getY() <= level.getMinBuildHeight() || feet.getY() >= level.getMaxBuildHeight() - 1) {
            return false;
        }

        BlockPos head = feet.above();
        BlockPos below = feet.below();

        BlockState feetState = level.getBlockState(feet);
        BlockState headState = level.getBlockState(head);
        BlockState belowState = level.getBlockState(below);

        if (!feetState.getCollisionShape(level, feet).isEmpty()) {
            return false;
        }
        if (!headState.getCollisionShape(level, head).isEmpty()) {
            return false;
        }

        if (!level.getFluidState(feet).isEmpty() || !level.getFluidState(head).isEmpty()) {
            return false;
        }

        if (belowState.isAir()) {
            return false;
        }

        if (!belowState.isFaceSturdy(level, below, Direction.UP)) {
            return false;
        }

        return !isDangerous(feetState) && !isDangerous(headState) && !isDangerous(belowState);
    }

    private static boolean isDangerous(final BlockState state) {
        if (state.is(Blocks.LAVA) || state.is(Blocks.MAGMA_BLOCK) || state.is(Blocks.CACTUS)
                || state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE)
                || state.is(Blocks.SWEET_BERRY_BUSH) || state.is(Blocks.WITHER_ROSE)) {
            return true;
        }

        if (state.getBlock() instanceof CampfireBlock && state.getValue(CampfireBlock.LIT)) {
            return true;
        }

        return !state.getFluidState().isEmpty();
    }
}