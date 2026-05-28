package com.denfop.blockentity.storage;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.storage.Import;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockStorageSystemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityFluidImportBus extends BlockEntityBus implements Import {


    public BlockEntityFluidImportBus(BlockPos pos, BlockState state) {
        super(BlockStorageSystemEntity.fluid_importbus, true, true, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockStorageSystemEntity.fluid_importbus;
    }

    public BlockTileEntity getBlock() {
        return IUItem.storageSystem.getBlock(getTeBlock());
    }

}
