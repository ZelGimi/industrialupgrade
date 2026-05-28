package com.denfop.ability;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

import java.util.*;

public final class AbilityVeinSearch {

    static TagKey<Block> ORES = TagKey.create(BuiltInRegistries.BLOCK.key(), ResourceLocation.tryBuild("c", "ores"));

    private AbilityVeinSearch() {
    }

    public static List<BlockPos> collectVein(
            final ServerPlayer player,
            final ItemStack stack,
            final BlockPos origin,
            final int maxBlocks,
            final int maxHorizontalRadius,
            final int maxVerticalRadius
    ) {
        if (player == null || stack.isEmpty()) {
            return List.of();
        }

        if (!AbilityToolHelper.isSupportedVanillaPickaxe(stack)) {
            return List.of();
        }

        if (!player.level().hasChunkAt(origin)) {
            return List.of();
        }

        final BlockState originState = player.level().getBlockState(origin);
        if (originState.isAir()) {
            return List.of();
        }


        if (!originState.is(ORES) && !originState.is(Tags.Blocks.ORES)) {
            return List.of();
        }

        if (!stack.isCorrectToolForDrops(originState)) {
            return List.of();
        }

        final Block originBlock = originState.getBlock();
        final ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        final List<BlockPos> result = new ArrayList<>(Math.min(maxBlocks, 128));
        final Set<Long> visited = new HashSet<>();

        queue.add(origin.immutable());
        visited.add(origin.asLong());

        while (!queue.isEmpty() && result.size() < maxBlocks) {
            final BlockPos current = queue.poll();
            if (current == null) {
                continue;
            }

            if (!player.level().hasChunkAt(current)) {
                continue;
            }

            if (Math.abs(current.getX() - origin.getX()) > maxHorizontalRadius
                    || Math.abs(current.getY() - origin.getY()) > maxVerticalRadius
                    || Math.abs(current.getZ() - origin.getZ()) > maxHorizontalRadius) {
                continue;
            }

            final BlockState state = player.level().getBlockState(current);
            if (state.isAir()) {
                continue;
            }


            if (!state.is(ORES) && !state.is(Tags.Blocks.ORES)) {
                continue;
            }

            if (state.liquid()) {
                continue;
            }

            if (state.getDestroySpeed(player.level(), current) < 0.0F) {
                continue;
            }

            if (!stack.isCorrectToolForDrops(state)) {
                continue;
            }

            result.add(current.immutable());

            for (final Direction direction : Direction.values()) {
                final BlockPos next = current.relative(direction);
                if (visited.add(next.asLong())) {
                    queue.add(next.immutable());
                }
            }
        }

        return result;
    }
}