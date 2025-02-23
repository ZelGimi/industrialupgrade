package com.denfop.tiles.mechanism.combpump;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;

public class TileEntitySimpleCombinedPump extends TileEntityCombinedPump {

    public TileEntitySimpleCombinedPump() {
        super(40, 40, EnumTypePump.S);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.comb_pump;
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

}
