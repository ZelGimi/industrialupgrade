package com.denfop.tiles.transport.tiles.exp;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockExpCable;
import com.denfop.tiles.transport.types.ExpType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityExpCable extends com.denfop.tiles.transport.tiles.TileEntityExpPipes {
    public TileEntityExpCable(BlockPos pos, BlockState state) {
        super(ExpType.expcable, BlockExpCable.experience, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockExpCable.experience;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.expcable.getBlock(getTeBlock().getId());
    }
}
