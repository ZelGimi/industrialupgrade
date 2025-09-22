package com.denfop.tiles.mechanism.generator.energy.redstone;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;

public class TileRedstoneGenerator extends TileBaseRedstoneGenerator {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileRedstoneGenerator() {
        super(1, 1);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.3));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.4));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.redstone_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
