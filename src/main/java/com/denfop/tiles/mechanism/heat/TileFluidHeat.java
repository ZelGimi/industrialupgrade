package com.denfop.tiles.mechanism.heat;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.tiles.base.TileBaseHeatMachine;

public class TileFluidHeat extends TileBaseHeatMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileFluidHeat() {
        super(true);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.24));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.4));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.fluid_heat;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
