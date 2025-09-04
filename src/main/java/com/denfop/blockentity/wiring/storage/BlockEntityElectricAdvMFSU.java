package com.denfop.blockentity.wiring.storage;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityElectricBlock;
import com.denfop.blockentity.wiring.EnumElectricBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEnergyStorageEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityElectricAdvMFSU extends BlockEntityElectricBlock {

    public BlockEntityElectricAdvMFSU(BlockPos pos, BlockState state) {
        super(EnumElectricBlock.ADV_MFSU, BlockEnergyStorageEntity.adv_mfsu, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockEnergyStorageEntity.adv_mfsu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.electricblock.getBlock(getTeBlock().getId());
    }

}
