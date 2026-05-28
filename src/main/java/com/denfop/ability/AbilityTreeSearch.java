package com.denfop.ability;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public final class AbilityTreeSearch {

    private AbilityTreeSearch() {
    }

    public static List<BlockPos> collectTree(
            final ServerPlayer player,
            final ItemStack stack,
            final BlockPos origin,
            final int maxLogs
    ) {
        if (player == null || stack.isEmpty()) {
            return List.of();
        }

        if (!AbilityToolHelper.isSupportedVanillaAxe(stack)) {
            return List.of();
        }

        if (!player.level().hasChunkAt(origin)) {
            return List.of();
        }

        final BlockState originState = player.level().getBlockState(origin);
        if (!originState.is(BlockTags.LOGS)) {
            return List.of();
        }

        final ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        final List<BlockPos> logs = new ArrayList<>(Math.min(maxLogs, 160));
        final Set<Long> visited = new HashSet<>();

        queue.add(origin.immutable());
        visited.add(origin.asLong());

        while (!queue.isEmpty() && logs.size() < maxLogs) {
            final BlockPos current = queue.poll();
            if (current == null) {
                continue;
            }

            if (!player.level().hasChunkAt(current)) {
                continue;
            }

            if (current.getY() < origin.getY() - 1 || current.getY() > origin.getY() + 48) {
                continue;
            }

            if (Math.abs(current.getX() - origin.getX()) > 7 || Math.abs(current.getZ() - origin.getZ()) > 7) {
                continue;
            }

            final BlockState state = player.level().getBlockState(current);
            if (!state.is(BlockTags.LOGS)) {
                continue;
            }

            if (state.getDestroySpeed(player.level(), current) < 0.0F) {
                continue;
            }

            logs.add(current.immutable());

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dy == 0 && dz == 0) {
                            continue;
                        }

                        final BlockPos next = current.offset(dx, dy, dz);
                        if (visited.add(next.asLong())) {
                            queue.add(next.immutable());
                        }
                    }
                }
            }
        }

        if (!isNaturalTree(player, logs)) {
            return List.of();
        }

        logs.sort((a, b) -> Integer.compare(b.getY(), a.getY()));
        return logs;
    }

    private static boolean isNaturalTree(final ServerPlayer player, final List<BlockPos> logs) {
        if (logs.size() < 3) {
            return false;
        }

        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (final BlockPos pos : logs) {
            minY = Math.min(minY, pos.getY());
            maxY = Math.max(maxY, pos.getY());
        }

        final int middleY = (minY + maxY) / 2;
        int leavesCount = 0;
        final Set<Long> leavesVisited = new HashSet<>();

        for (final BlockPos logPos : logs) {
            if (logPos.getY() < middleY) {
                continue;
            }

            for (int dx = -2; dx <= 2; dx++) {
                for (int dy = -2; dy <= 2; dy++) {
                    for (int dz = -2; dz <= 2; dz++) {
                        final BlockPos checkPos = logPos.offset(dx, dy, dz);

                        if (!player.level().hasChunkAt(checkPos)) {
                            continue;
                        }

                        if (player.level().getBlockState(checkPos).is(BlockTags.LEAVES)) {
                            if (leavesVisited.add(checkPos.asLong())) {
                                leavesCount++;
                                if (leavesCount >= 6) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return logs.size() >= 6 && leavesCount >= 3;
    }
}
