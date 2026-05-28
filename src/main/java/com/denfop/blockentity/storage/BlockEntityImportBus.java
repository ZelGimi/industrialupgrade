package com.denfop.blockentity.storage;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.storage.Import;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockStorageSystemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImportBus extends BlockEntityBus implements Import {

    public BlockEntityImportBus(BlockPos pos, BlockState state) {
        super(BlockStorageSystemEntity.importbus, false, true, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockStorageSystemEntity.importbus;
    }

    public BlockTileEntity getBlock() {
        return IUItem.storageSystem.getBlock(getTeBlock());
    }

}
