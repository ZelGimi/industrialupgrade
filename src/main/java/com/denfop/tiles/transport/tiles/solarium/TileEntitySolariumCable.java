package com.denfop.tiles.transport.tiles.solarium;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSolariumCable;
import com.denfop.tiles.transport.tiles.TileEntitySCable;
import com.denfop.tiles.transport.types.SEType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntitySolariumCable extends TileEntitySCable {
    public TileEntitySolariumCable(BlockPos pos, BlockState state) {
        super(SEType.scable, BlockSolariumCable.solarium, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSolariumCable.solarium;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.scable.getBlock(getTeBlock().getId());
    }
}
