package com.denfop.tiles.transformer;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.tiles.base.TileTransformer;

public class TileUEVTransformer extends TileTransformer {

    public TileUEVTransformer() {
        super(7);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockTransformer.uev;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tranformer;
    }

}
