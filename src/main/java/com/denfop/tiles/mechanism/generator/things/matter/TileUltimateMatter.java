package com.denfop.tiles.mechanism.generator.things.matter;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.tiles.base.TileMultiMatter;

public class TileUltimateMatter extends TileMultiMatter {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileUltimateMatter() {
        super(700000F, 16, 256000000);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.011));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.045));
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.per_matter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines;
    }

}
