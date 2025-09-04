package com.denfop.blockentity.creative;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityElectricBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCreativeBlocksEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityCreativeMFSU extends BlockEntityElectricBlock {
    public BlockEntityCreativeMFSU(BlockPos pos, BlockState state) {
        super(14, Math.pow(10, 24), Math.pow(10, 24), false, "", BlockCreativeBlocksEntity.creative_mfsu, pos, state);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.creativeBlock.getBlock(getTeBlock());
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        this.energy.buffer.storage = this.energy.buffer.capacity;
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockCreativeBlocksEntity.creative_mfsu;
    }
}
