package com.denfop.tiles.transformer;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.tiles.base.TileTransformer;

public class TileTransformerLV extends TileTransformer {

    public TileTransformerLV() {
        super(1);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockTransformer.lv;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tranformer;
    }

}
