package com.denfop.tiles.transport.tiles.transport;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransportPipe;
import com.denfop.tiles.transport.tiles.TileEntityItemPipes;
import com.denfop.tiles.transport.types.ItemType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityTransportPipe22 extends TileEntityItemPipes {
    public TileEntityTransportPipe22(BlockPos pos, BlockState state) {
        super(ItemType.itemcable22, BlockTransportPipe.itemcable22, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockTransportPipe.itemcable22;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.item_pipes.getBlock(getTeBlock().getId());
    }
}
