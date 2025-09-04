package com.denfop.blockentity.lightning_rod;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockLightningRodEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityLightningRodReceiver extends BlockEntityMultiBlockElement implements IReceiver {


    public BlockEntityLightningRodReceiver(BlockPos pos, BlockState state) {
        super(BlockLightningRodEntity.lightning_rod_receiver, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockLightningRodEntity.lightning_rod_receiver;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.lightning_rod.getBlock(getTeBlock());
    }


}
