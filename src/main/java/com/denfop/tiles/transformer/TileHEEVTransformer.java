package com.denfop.tiles.transformer;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.tiles.base.TileTransformer;

public class TileHEEVTransformer extends TileTransformer {

    public TileHEEVTransformer() {
        super(11);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockTransformer.heev;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tranformer;
    }

}
