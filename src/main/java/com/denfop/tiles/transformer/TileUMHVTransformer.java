package com.denfop.tiles.transformer;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.tiles.base.TileTransformer;

public class TileUMHVTransformer extends TileTransformer {

    public TileUMHVTransformer() {
        super(8);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockTransformer.umhv;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tranformer;
    }

}
