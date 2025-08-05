package com.denfop.tiles.transformer;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.tiles.base.TileTransformer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileUHVTransformer extends TileTransformer {

    public TileUHVTransformer(BlockPos pos, BlockState state) {
        super(6, BlockTransformer.uhv, pos, state);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockTransformer.uhv;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tranformer.getBlock(getTeBlock().getId());
    }
}
