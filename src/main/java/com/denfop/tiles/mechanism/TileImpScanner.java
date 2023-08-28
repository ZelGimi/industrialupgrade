package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileScanner;

public class TileImpScanner extends TileScanner {

    public TileImpScanner() {
        super(2000);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.imp_scanner;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
