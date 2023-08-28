package com.denfop.tiles.transformer;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.tiles.base.TileTransformer;

public class TileUMEVTransformer extends TileTransformer {

    public TileUMEVTransformer() {
        super(9);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockTransformer.umev;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tranformer;
    }

}
