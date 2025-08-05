package com.denfop.tiles.transformer;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.tiles.base.TileTransformer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileHEEVTransformer extends TileTransformer {

    public TileHEEVTransformer(BlockPos pos, BlockState state) {
        super(11, BlockTransformer.heev, pos, state);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockTransformer.heev;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tranformer.getBlock(getTeBlock().getId());
    }

}
