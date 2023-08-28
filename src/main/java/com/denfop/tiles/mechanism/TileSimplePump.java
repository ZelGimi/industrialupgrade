package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;

public class TileSimplePump extends TilePump {

    public TileSimplePump() {
        super(10, 20);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.pump_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
