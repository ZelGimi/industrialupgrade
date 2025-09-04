package com.denfop.blockentity.wiring.storage;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityElectricBlock;
import com.denfop.blockentity.wiring.EnumElectricBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEnergyStorageEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityElectricBarMFSU extends BlockEntityElectricBlock {

    public BlockEntityElectricBarMFSU(BlockPos pos, BlockState state) {
        super(EnumElectricBlock.BAR_MFSU, BlockEnergyStorageEntity.bar_mfsu, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockEnergyStorageEntity.bar_mfsu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.electricblock.getBlock(getTeBlock().getId());
    }

}
