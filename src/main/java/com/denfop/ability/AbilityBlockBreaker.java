package com.denfop.ability;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class AbilityBlockBreaker {

    private AbilityBlockBreaker() {
    }

    public static int breakBlocks(
            final ServerPlayer player,
            final ItemStack stack,
            final List<BlockPos> positions
    ) {
        if (player == null || stack.isEmpty() || positions.isEmpty()) {
            return 0;
        }

        int broken = 0;

        for (final BlockPos pos : positions) {
            if (!player.level().hasChunkAt(pos)) {
                continue;
            }

            if (player.level().getBlockState(pos).isAir()) {
                continue;
            }


            if (player.gameMode.destroyBlock(pos)) {
                broken++;
            }

            if (stack.isEmpty()) {
                break;
            }
        }

        return broken;
    }
}