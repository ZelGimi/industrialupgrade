package com.denfop.tiles.reactors.water.outputfluid;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import com.denfop.tiles.reactors.water.IOutput;

public class TileEntityPerOutputPort extends TileEntityOutputFluid implements IOutput {

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_per_output;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component;
    }

    @Override
    public int getLevel() {
        return 3;
    }

}
