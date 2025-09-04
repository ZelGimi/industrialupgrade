package com.denfop.blockentity.transport.tiles.transport;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.transport.tiles.BlockEntityItemPipes;
import com.denfop.blockentity.transport.types.ItemType;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransportPipeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityTransportPipe4 extends BlockEntityItemPipes {
    public BlockEntityTransportPipe4(BlockPos pos, BlockState state) {
        super(ItemType.itemcable4, BlockTransportPipeEntity.itemcable4, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockTransportPipeEntity.itemcable4;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.item_pipes.getBlock(getTeBlock().getId());
    }
}
