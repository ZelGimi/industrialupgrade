package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.tiles.base.TileBaseHandlerHeavyOre;

public class TileAdvHandlerHeavyOre extends TileBaseHandlerHeavyOre {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileAdvHandlerHeavyOre() {
        super(EnumTypeStyle.ADVANCED);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.35));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.8));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.double_handlerho;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
