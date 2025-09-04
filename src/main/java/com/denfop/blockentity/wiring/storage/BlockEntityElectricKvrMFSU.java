package com.denfop.blockentity.wiring.storage;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityElectricBlock;
import com.denfop.blockentity.wiring.EnumElectricBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEnergyStorageEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityElectricKvrMFSU extends BlockEntityElectricBlock {

    public BlockEntityElectricKvrMFSU(BlockPos pos, BlockState state) {
        super(EnumElectricBlock.KVR_MFSU, BlockEnergyStorageEntity.qua_mfsu, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockEnergyStorageEntity.qua_mfsu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.electricblock.getBlock(getTeBlock().getId());
    }

}
