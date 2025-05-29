package com.denfop.tiles.transport.tiles.energycable;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCable;
import com.denfop.tiles.transport.tiles.TileEntityCable;
import com.denfop.tiles.transport.types.CableType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityGlass5Cable extends TileEntityCable {
    public TileEntityGlass5Cable(BlockPos pos, BlockState state) {
        super(CableType.glass5, BlockCable.glass5, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockCable.glass5;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.cable.getBlock(getTeBlock().getId());
    }
}
