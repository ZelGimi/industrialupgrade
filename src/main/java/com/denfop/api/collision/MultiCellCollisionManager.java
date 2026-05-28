package com.denfop.api.collision;

import com.denfop.IUItem;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.collision.BlockEntityCollisionProxy;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;

import static com.denfop.IUItem.COLLISION_PROXY;

public final class MultiCellCollisionManager {

    private MultiCellCollisionManager() {
    }

    public static void refresh(Level level, BlockPos masterPos) {
        if (level == null || level.isClientSide) {
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(masterPos);
        if (!(blockEntity instanceof BlockEntityBase master)) {
            removeAll(level, masterPos);
            return;
        }

        IMultiCellCollisionProvider provider = master;
        if (!provider.useMultiCellCollision()) {
            removeAll(level, masterPos);
            return;
        }

        Set<BlockPos> desired = MultiCellCollisionShapeHelper.collectCoveredCells(master, masterPos, true);
        Set<BlockPos> existing = collectExisting(level, masterPos);

        for (BlockPos pos : existing) {
            if (!desired.contains(pos)) {
                BlockState state = level.getBlockState(pos);
                if (state.is(COLLISION_PROXY.get())) {
                    level.removeBlock(pos, false);
                }
            }
        }

        for (BlockPos pos : desired) {
            BlockState state = level.getBlockState(pos);

            if (pos.equals(masterPos)) {
                continue;
            }

            if (state.isAir()) {
                level.setBlock(pos, COLLISION_PROXY.get().defaultBlockState(), 3);
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof BlockEntityCollisionProxy proxy) {
                    proxy.setMasterPos(masterPos);
                }
            } else if (state.is(COLLISION_PROXY.get())) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof BlockEntityCollisionProxy proxy) {
                    proxy.setMasterPos(masterPos);
                }
            }
        }
    }

    public static void removeAll(Level level, BlockPos masterPos) {
        if (level == null || level.isClientSide) {
            return;
        }

        Set<BlockPos> existing = collectExisting(level, masterPos);
        for (BlockPos pos : existing) {
            BlockState state = level.getBlockState(pos);
            if (state.is(IUItem.COLLISION_PROXY.get())) {
                level.removeBlock(pos, false);
            }
        }
    }

    private static Set<BlockPos> collectExisting(Level level, BlockPos masterPos) {
        Set<BlockPos> result = new HashSet<>();

        for (int x = -4; x <= 4; x++) {
            for (int y = -4; y <= 4; y++) {
                for (int z = -4; z <= 4; z++) {
                    BlockPos pos = masterPos.offset(x, y, z);
                    BlockState state = level.getBlockState(pos);
                    if (!state.is(IUItem.COLLISION_PROXY.get())) {
                        continue;
                    }

                    BlockEntity be = level.getBlockEntity(pos);
                    if (be instanceof BlockEntityCollisionProxy proxy && masterPos.equals(proxy.getMasterPos())) {
                        result.add(pos);
                    }
                }
            }
        }

        return result;
    }
}