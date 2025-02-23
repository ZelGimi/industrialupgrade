package com.denfop.tiles.mechanism.generator.energy.fluid;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;

public class TileEntityGeoGenerator extends com.denfop.tiles.mechanism.generator.energy.TileEntityGeoGenerator {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileEntityGeoGenerator() {
        super(10, 1, 1);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.35));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.85));
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.geogenerator_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
