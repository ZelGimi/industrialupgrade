package com.denfop.tiles.transport.tiles.amperecable;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockAmpereCable;
import com.denfop.tiles.transport.types.AmpereType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityAmpereCable extends com.denfop.tiles.transport.tiles.TileEntityAmpereCable {
    public TileEntityAmpereCable(BlockPos pos, BlockState state) {
        super(AmpereType.acable, BlockAmpereCable.ampere, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockAmpereCable.ampere;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.amperepipes.getBlock(getTeBlock().getId());
    }
}
