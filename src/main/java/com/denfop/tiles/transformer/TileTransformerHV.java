package com.denfop.tiles.transformer;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.tiles.base.TileTransformer;

public class TileTransformerHV extends TileTransformer {

    public TileTransformerHV() {
        super(3);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockTransformer.hv;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tranformer;
    }

}
