package com.denfop.blockentity.wiring.chargepad;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityElectricBlock;
import com.denfop.blockentity.wiring.EnumElectricBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockChargepadStorageEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityChargepadKvrMFSU extends BlockEntityElectricBlock {

    public BlockEntityChargepadKvrMFSU(BlockPos pos, BlockState state) {
        super(EnumElectricBlock.KVR_MFSU_CHARGEPAD, BlockChargepadStorageEntity.qua_mfsu_chargepad, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockChargepadStorageEntity.qua_mfsu_chargepad;
    }

    public BlockTileEntity getBlock() {
        return IUItem.chargepadelectricblock.getBlock(getTeBlock().getId());
    }
}
