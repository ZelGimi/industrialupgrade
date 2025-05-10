package com.denfop.tiles.transport.tiles.transport;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransportPipe;
import com.denfop.tiles.transport.tiles.TileEntityItemPipes;
import com.denfop.tiles.transport.types.ItemType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityTransportPipe25 extends TileEntityItemPipes {
    public TileEntityTransportPipe25(BlockPos pos, BlockState state) {
        super(ItemType.itemcable25, BlockTransportPipe.itemcable25, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockTransportPipe.itemcable25;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.item_pipes.getBlock(getTeBlock().getId());
    }
}
