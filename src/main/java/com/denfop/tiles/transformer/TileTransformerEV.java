package com.denfop.tiles.transformer;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.tiles.base.TileTransformer;

public class TileTransformerEV extends TileTransformer {

    public TileTransformerEV() {
        super(4);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockTransformer.ev;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tranformer;
    }

}
