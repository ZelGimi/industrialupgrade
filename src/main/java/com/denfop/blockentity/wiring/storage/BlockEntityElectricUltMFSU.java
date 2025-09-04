package com.denfop.blockentity.wiring.storage;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityElectricBlock;
import com.denfop.blockentity.wiring.EnumElectricBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEnergyStorageEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityElectricUltMFSU extends BlockEntityElectricBlock {

    public BlockEntityElectricUltMFSU(BlockPos pos, BlockState state) {
        super(EnumElectricBlock.ULT_MFSU, BlockEnergyStorageEntity.ult_mfsu, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockEnergyStorageEntity.ult_mfsu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.electricblock.getBlock(getTeBlock().getId());
    }

}
