package com.denfop.tiles.transformer;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.tiles.base.TileTransformer;

public class TileUHEVTransformer extends TileTransformer {

    public TileUHEVTransformer() {
        super(10);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockTransformer.uhev;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tranformer;
    }

}
