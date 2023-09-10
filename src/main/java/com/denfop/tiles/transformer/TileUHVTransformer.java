package com.denfop.tiles.transformer;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.tiles.base.TileTransformer;

public class TileUHVTransformer extends TileTransformer {

    public TileUHVTransformer() {
        super(6);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockTransformer.uhv;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tranformer;
    }

}
