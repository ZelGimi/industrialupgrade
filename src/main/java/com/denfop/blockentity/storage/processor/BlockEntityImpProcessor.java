package com.denfop.blockentity.storage.processor;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.storage.BlockEntityBaseProcessor;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockStorageSystemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpProcessor extends BlockEntityBaseProcessor {
    public BlockEntityImpProcessor(BlockPos pos, BlockState state) {
        super(BlockStorageSystemEntity.imp_processor, 4, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockStorageSystemEntity.imp_processor;
    }

    public BlockTileEntity getBlock() {
        return IUItem.storageSystem.getBlock(getTeBlock());
    }
}
