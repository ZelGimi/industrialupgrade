package com.denfop.tiles.mechanism.wind;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.windsystem.EnumLevelGenerators;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;

public class TileAdvWindGenerator extends TileWindGenerator {

    private final AirPollutionComponent pollutionAir;

    public TileAdvWindGenerator() {
        super(EnumLevelGenerators.TWO);
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.05));
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.adv_wind_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
