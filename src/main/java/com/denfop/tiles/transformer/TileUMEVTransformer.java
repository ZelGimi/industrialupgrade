package com.denfop.tiles.transformer;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.tiles.base.TileTransformer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileUMEVTransformer extends TileTransformer {

    public TileUMEVTransformer(BlockPos pos, BlockState state) {
        super(9, BlockTransformer.umev,pos,state);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockTransformer.umev;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tranformer.getBlock(getTeBlock().getId());
    }
}
