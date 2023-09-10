package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileBaseHandlerHeavyOre;

public class TilePerHandlerHeavyOre extends TileBaseHandlerHeavyOre {

    public TilePerHandlerHeavyOre() {
        super(EnumTypeStyle.PERFECT);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.quad_handlerho;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
