package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileScanner;

public class TileSimpleScanner extends TileScanner {

    public TileSimpleScanner() {
        super(3300);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.scanner_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
