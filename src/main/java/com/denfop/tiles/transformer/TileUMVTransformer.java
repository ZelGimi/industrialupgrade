package com.denfop.tiles.transformer;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.tiles.base.TileTransformer;

public class TileUMVTransformer extends TileTransformer {

    public TileUMVTransformer() {
        super(5);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockTransformer.umv;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tranformer;
    }

}
