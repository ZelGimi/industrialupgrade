package com.denfop.tiles.mechanism.solardestiller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;

public class TileEntitySolarDestiller extends TileEntityBaseSolarDestiller {

    public TileEntitySolarDestiller() {
        super(EnumTypeStyle.DEFAULT);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.solardestiller;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
