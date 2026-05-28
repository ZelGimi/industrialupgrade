package com.denfop.blockentity.storage;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.storage.Export;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockStorageSystemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityExportBus extends BlockEntityBus implements Export {


    public BlockEntityExportBus(BlockPos pos, BlockState state) {
        super(BlockStorageSystemEntity.exportbus, false, false, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockStorageSystemEntity.exportbus;
    }

    public BlockTileEntity getBlock() {
        return IUItem.storageSystem.getBlock(getTeBlock());
    }


}
