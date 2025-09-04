package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.componets.EnumTypeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPhotonicReplicator extends BlockEntityBaseReplicator {


    public BlockEntityPhotonicReplicator(BlockPos pos, BlockState state) {
        super(0.7, BlocksPhotonicMachine.photonic_replicator, pos, state);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PHOTONIC;
    }

    public MultiBlockEntity getTeBlock() {
        return BlocksPhotonicMachine.photonic_replicator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine.getBlock(getTeBlock().getId());
    }

}
