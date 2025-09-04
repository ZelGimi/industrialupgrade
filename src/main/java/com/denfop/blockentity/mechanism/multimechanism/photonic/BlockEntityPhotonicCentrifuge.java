package com.denfop.blockentity.mechanism.multimechanism.photonic;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPhotonicCentrifuge extends BlockEntityMultiMachine {

    public BlockEntityPhotonicCentrifuge(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.Pho_Centrifuge, BlocksPhotonicMachine.photonic_centrifuge, pos, state
        );

    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        this.cold.buffer.storage = 0;
    }

    public MultiBlockEntity getTeBlock() {
        return BlocksPhotonicMachine.photonic_centrifuge;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine.getBlock(getTeBlock().getId());
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Pho_Centrifuge;
    }


}
