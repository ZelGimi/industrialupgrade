package com.denfop.tiles.geothermalpump;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGeothermalPump;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;

public class TileEntityGeothermalRigDrill extends TileEntityMultiBlockElement implements IRig {
    public TileEntityGeothermalRigDrill(){}
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGeothermalPump.geothermal_rig;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.geothermalpump;
    }

}
