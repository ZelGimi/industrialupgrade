package com.denfop.tiles.transformer;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.tiles.base.TileTransformer;

public class TileTransformerMV extends TileTransformer {

    public TileTransformerMV() {
        super(2);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockTransformer.mv;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tranformer;
    }

}
