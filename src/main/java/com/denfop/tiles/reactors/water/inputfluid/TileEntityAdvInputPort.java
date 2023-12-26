package com.denfop.tiles.reactors.water.inputfluid;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import com.denfop.tiles.base.TileEntityBlock;

public class TileEntityAdvInputPort  extends TileEntityInputFluid {
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_adv_input;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component;
    }
    @Override
    public int getLevel() {
        return 1;
    }
}
