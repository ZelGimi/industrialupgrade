package com.denfop.tiles.mechanism.generator.energy.redstone;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;

public class TileImpRedstoneGenerator extends TileBaseRedstoneGenerator {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileImpRedstoneGenerator() {
        super(3.4, 4);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.15));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.2));
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.imp_redstone_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
