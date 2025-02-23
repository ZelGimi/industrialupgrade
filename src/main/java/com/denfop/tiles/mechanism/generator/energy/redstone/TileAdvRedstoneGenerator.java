package com.denfop.tiles.mechanism.generator.energy.redstone;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;

public class TileAdvRedstoneGenerator extends TileBaseRedstoneGenerator {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileAdvRedstoneGenerator() {
        super(2.2, 2);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.2));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.3));
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.adv_redstone_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
