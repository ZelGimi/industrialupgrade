package com.denfop.tiles.reactors.heat.socket;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityAdvSocket extends TileEntityMainSocket {

    public TileEntityAdvSocket(BlockPos pos, BlockState state) {
        super(20000,BlockHeatReactor.heat_adv_socket,pos,state);
    }

    @Override
    public int getBlockLevel() {
        return 1;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_adv_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor.getBlock(getTeBlock());
    }

}
