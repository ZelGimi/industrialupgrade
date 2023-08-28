package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.TilePump;

public class TileAdvPump extends TilePump {

    public TileAdvPump() {
        super(10, 15);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine2.adv_pump;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1;
    }

}
