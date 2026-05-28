package com.denfop.blockentity.storage.processor;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.storage.BlockEntityBaseProcessor;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockStorageSystemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySimpleProcessor extends BlockEntityBaseProcessor {
    public BlockEntitySimpleProcessor(BlockPos pos, BlockState state) {
        super(BlockStorageSystemEntity.processor, 1, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockStorageSystemEntity.processor;
    }

    public BlockTileEntity getBlock() {
        return IUItem.storageSystem.getBlock(getTeBlock());
    }
}
