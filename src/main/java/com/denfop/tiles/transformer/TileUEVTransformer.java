package com.denfop.tiles.transformer;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.tiles.base.TileTransformer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileUEVTransformer extends TileTransformer {

    public TileUEVTransformer(BlockPos pos, BlockState state) {
        super(7, BlockTransformer.uev,pos,state);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockTransformer.uev;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tranformer.getBlock(getTeBlock().getId());
    }
}
