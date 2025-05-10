package com.denfop.tiles.transport.tiles.night;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockNightCable;
import com.denfop.tiles.transport.types.NightType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityNightCable extends com.denfop.tiles.transport.tiles.TileEntityNightPipe {
    public TileEntityNightCable(BlockPos pos, BlockState state) {
        super(NightType.npipe, BlockNightCable.night, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockNightCable.night;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.nightpipes.getBlock(getTeBlock().getId());
    }
}
