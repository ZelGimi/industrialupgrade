package com.denfop.api.space.dimension.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public final class SpaceTerrainSupportHelper {

    private SpaceTerrainSupportHelper() {
    }

    public static Map<Long, Integer> createColumnTracker() {
        return new HashMap<>();
    }

    public static void placeTrackedSolid(
            final WorldGenLevel level,
            final BlockPos pos,
            final BlockState state,
            final Map<Long, Integer> lowestByColumn
    ) {
        if (state == null || state.isAir()) {
            return;
        }

        level.setBlock(pos, state, 3);

        if (state.getFluidState().isEmpty()) {
            final long key = pack(pos.getX(), pos.getZ());
            lowestByColumn.merge(key, pos.getY(), Math::min);
        }
    }

    public static int findSurfaceY(
            final WorldGenLevel level,
            final int x,
            final int z,
            final int startY
    ) {
        final int maxY = Math.min(startY, level.getMaxBuildHeight() - 1);
        final int minY = level.getMinBuildHeight();

        final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(x, maxY, z);

        while (cursor.getY() > minY) {
            final BlockState state = level.getBlockState(cursor);
            if (isGround(state)) {
                return cursor.getY();
            }
            cursor.move(0, -1, 0);
        }

        return minY;
    }

    public static void stitchUnsupportedColumnsDown(
            final WorldGenLevel level,
            final Map<Long, Integer> lowestByColumn,
            final BlockPos center,
            final int craterRadius,
            final BlockState mainFill,
            final BlockState edgeFill
    ) {
        final int minY = level.getMinBuildHeight();
        final int craterRadiusSq = Math.max(0, craterRadius - 1) * Math.max(0, craterRadius - 1);

        final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (Map.Entry<Long, Integer> entry : lowestByColumn.entrySet()) {
            final int x = unpackX(entry.getKey());
            final int z = unpackZ(entry.getKey());
            final int lowestY = entry.getValue();

            final int dx = x - center.getX();
            final int dz = z - center.getZ();
            final int distSq = dx * dx + dz * dz;

            if (distSq <= craterRadiusSq) {
                continue;
            }

            int y = lowestY - 1;
            int supportDepth = 0;

            while (y >= minY) {
                cursor.set(x, y, z);
                final BlockState current = level.getBlockState(cursor);

                if (!canReplaceForSupport(current)) {
                    break;
                }

                final BlockState fill = supportDepth < 2 ? edgeFill : mainFill;
                level.setBlock(cursor, fill, 3);

                supportDepth++;
                y--;
            }
        }
    }

    public static boolean isGround(final BlockState state) {
        return !state.isAir() && state.getFluidState().isEmpty() && state.canOcclude();
    }

    public static boolean canReplaceForSupport(final BlockState state) {
        if (state.isAir()) {
            return true;
        }
        if (!state.getFluidState().isEmpty()) {
            return true;
        }
        return !state.canOcclude();
    }

    private static long pack(final int x, final int z) {
        return ((long) x << 32) | (z & 0xFFFFFFFFL);
    }

    private static int unpackX(final long packed) {
        return (int) (packed >> 32);
    }

    private static int unpackZ(final long packed) {
        return (int) packed;
    }
}