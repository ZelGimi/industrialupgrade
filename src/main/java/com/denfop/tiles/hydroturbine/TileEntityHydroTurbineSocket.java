package com.denfop.tiles.hydroturbine;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCyclotron;
import com.denfop.blocks.mechanism.BlockHydroTurbine;
import com.denfop.blocks.mechanism.BlockWindTurbine;
import com.denfop.componets.Energy;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;

public class TileEntityHydroTurbineSocket extends TileEntityMultiBlockElement implements ISocket {

    private final Energy energy;

    public TileEntityHydroTurbineSocket(){
        this.energy= this.addComponent(Energy.asBasicSource(this,2000000,14));
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHydroTurbine.hydro_turbine_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.hydroTurbine;
    }

    @Override
    public Energy getEnergy() {
        return energy;
    }

}
