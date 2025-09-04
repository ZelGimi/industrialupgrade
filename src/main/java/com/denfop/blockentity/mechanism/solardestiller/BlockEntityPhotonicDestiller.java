package com.denfop.blockentity.mechanism.solardestiller;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPhotonicDestiller extends BlockEntityBaseSolarDestiller {

    public BlockEntityPhotonicDestiller(BlockPos pos, BlockState state) {
        super(EnumTypeStyle.PHOTONIC, BlocksPhotonicMachine.photonic_destiller, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlocksPhotonicMachine.photonic_destiller;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine.getBlock(getTeBlock().getId());
    }

}
