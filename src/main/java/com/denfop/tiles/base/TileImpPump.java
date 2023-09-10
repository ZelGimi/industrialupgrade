package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.TilePump;

public class TileImpPump extends TilePump {

    public TileImpPump() {
        super(15, 10);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine2.imp_pump;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1;
    }

}
