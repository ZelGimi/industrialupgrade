package com.denfop.tiles.mechanism.heat;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.tiles.base.TileBaseHeatMachine;

public class TileFluidHeat extends TileBaseHeatMachine {

    public TileFluidHeat() {
        super(true);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.fluid_heat;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
