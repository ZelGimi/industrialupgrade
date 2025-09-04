package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBaseHandlerHeavyOre;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPhotonicHandlerHO extends BlockEntityBaseHandlerHeavyOre {


    public BlockEntityPhotonicHandlerHO(BlockPos pos, BlockState state) {
        super(EnumTypeStyle.PHOTONIC, BlocksPhotonicMachine.photonic_handlerho, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlocksPhotonicMachine.photonic_handlerho;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine.getBlock(getTeBlock().getId());
    }

}
