package com.denfop.tiles.smeltery;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSmeltery;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntitySmelteryCasing extends TileEntityMultiBlockElement implements ICasing {

    public TileEntitySmelteryCasing(BlockPos pos, BlockState state) {
        super(BlockSmeltery.smeltery_casing, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSmeltery.smeltery_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.smeltery.getBlock(getTeBlock());
    }

}
