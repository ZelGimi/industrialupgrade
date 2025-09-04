package com.denfop.blockentity.wiring.chargepad;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityElectricBlock;
import com.denfop.blockentity.wiring.EnumElectricBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockChargepadStorageEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityChargepadMFSU extends BlockEntityElectricBlock {

    public BlockEntityChargepadMFSU(BlockPos pos, BlockState state) {
        super(EnumElectricBlock.MFSU_CHARGEPAD, BlockChargepadStorageEntity.mfsu_iu_chargepad, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockChargepadStorageEntity.mfsu_iu_chargepad;
    }

    public BlockTileEntity getBlock() {
        return IUItem.chargepadelectricblock.getBlock(getTeBlock().getId());
    }
}
