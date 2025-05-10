package com.denfop.tiles.transformer;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.tiles.base.TileTransformer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileUMHVTransformer extends TileTransformer {

    public TileUMHVTransformer(BlockPos pos, BlockState state) {
        super(8, BlockTransformer.umhv,pos,state);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockTransformer.umhv;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tranformer.getBlock(getTeBlock().getId());
    }
}
