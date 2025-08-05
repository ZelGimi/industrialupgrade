package com.denfop.tiles.creative;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCreativeBlocks;
import com.denfop.tiles.base.TileElectricBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityCreativeMFSU extends TileElectricBlock {
    public TileEntityCreativeMFSU(BlockPos pos, BlockState state) {
        super(14, Math.pow(10,24), Math.pow(10,24), false, "", BlockCreativeBlocks.creative_mfsu, pos, state);
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
    public IMultiTileBlock getTeBlock() {
        return  BlockCreativeBlocks.creative_mfsu;
    }
}
