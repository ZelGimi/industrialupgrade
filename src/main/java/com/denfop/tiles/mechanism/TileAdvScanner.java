package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileScanner;

public class TileAdvScanner extends TileScanner {

    public TileAdvScanner() {
        super(2500);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.adv_scanner;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
