package com.denfop.api.pollution.layer;

import com.denfop.blocks.AbstractLayerDustBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public final class EnvironmentalLayerHelper {

    private EnvironmentalLayerHelper() {
    }

    public static boolean tryAccumulate(ServerLevel level, ChunkPos chunkPos, Block block, float baseChance, boolean boostFromWeather) {
        int worldX = (chunkPos.x << 4) + level.random.nextInt(16);
        int worldZ = (chunkPos.z << 4) + level.random.nextInt(16);
        return tryAccumulate(level, worldX, worldZ, block, baseChance, boostFromWeather);
    }

    public static boolean tryAccumulate(ServerLevel level, int worldX, int worldZ, Block block, float baseChance, boolean boostFromWeather) {
        if (!(block instanceof AbstractLayerDustBlock<?>)) {
            return false;
        }

        int topY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, worldX, worldZ);
        if (topY < level.getMinBuildHeight() || topY >= level.getMaxBuildHeight()) {
            return false;
        }

        BlockPos.MutableBlockPos placePos = new BlockPos.MutableBlockPos(worldX, topY, worldZ);

        if (!level.canSeeSky(placePos)) {
            return false;
        }

        boolean precipitationBoost = boostFromWeather && level.isRainingAt(placePos);
        float chance = Math.min(1.0F, baseChance + (precipitationBoost ? 0.20F : 0.0F));

        if (level.random.nextFloat() > chance) {
            return false;
        }

        BlockState currentState = level.getBlockState(placePos);

        if (currentState.is(block)) {
            int currentLayers = currentState.getValue(AbstractLayerDustBlock.LAYERS);
            if (currentLayers >= 8) {
                return false;
            }

            level.setBlock(
                    placePos,
                    currentState.setValue(AbstractLayerDustBlock.LAYERS, currentLayers + 1),
                    3
            );
            return true;
        }

        if (!currentState.isAir() || !currentState.getFluidState().isEmpty()) {
            return false;
        }

        BlockPos.MutableBlockPos belowPos = new BlockPos.MutableBlockPos(worldX, topY - 1, worldZ);
        BlockState belowState = level.getBlockState(belowPos);

        if (!canSupportDust(level, belowPos, belowState, block)) {
            return false;
        }

        level.setBlock(
                placePos,
                block.defaultBlockState().setValue(AbstractLayerDustBlock.LAYERS, 1),
                3
        );
        return true;
    }

    private static boolean canSupportDust(ServerLevel level, BlockPos pos, BlockState state, Block block) {
        if (state.is(block)) {
            return state.getValue(AbstractLayerDustBlock.LAYERS) == 8;
        }

        if (!state.getFluidState().isEmpty()) {
            return false;
        }

        return state.isFaceSturdy(level, pos, Direction.UP);
    }
}