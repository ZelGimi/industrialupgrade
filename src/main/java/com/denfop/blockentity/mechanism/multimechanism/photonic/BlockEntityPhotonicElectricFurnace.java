package com.denfop.blockentity.mechanism.multimechanism.photonic;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPhotonicElectricFurnace extends BlockEntityMultiMachine {

    public BlockEntityPhotonicElectricFurnace(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.PHO_ELECTRIC_FURNACE, BlocksPhotonicMachine.photonic_furnace, pos, state
        );

    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        this.cold.buffer.storage = 0;
    }

    public MultiBlockEntity getTeBlock() {
        return BlocksPhotonicMachine.photonic_furnace;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine.getBlock(getTeBlock().getId());
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.PHO_ELECTRIC_FURNACE;
    }


}
