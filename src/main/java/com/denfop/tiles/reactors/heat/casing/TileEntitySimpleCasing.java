package com.denfop.tiles.reactors.heat.casing;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.heat.ICasing;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntitySimpleCasing extends TileEntityMultiBlockElement implements ICasing {

    public TileEntitySimpleCasing(BlockPos pos, BlockState state) {
        super(BlockHeatReactor.heat_casing, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor.getBlock(getTeBlock());
    }


    @Override
    public int getBlockLevel() {
        return 0;
    }

}
