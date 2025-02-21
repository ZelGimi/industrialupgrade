package com.denfop.tiles.windturbine;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCyclotron;
import com.denfop.blocks.mechanism.BlockWindTurbine;
import com.denfop.componets.Energy;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;

public class TileEntityWindTurbineSocket extends TileEntityMultiBlockElement implements ISocket {

    private final Energy energy;

    public TileEntityWindTurbineSocket(){
        this.energy= this.addComponent(Energy.asBasicSource(this,2000000,14));
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWindTurbine.wind_turbine_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.windTurbine;
    }

    @Override
    public Energy getEnergy() {
        return energy;
    }

}
