package com.denfop.tiles.mechanism.multimechanism.triple;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileTripleGearMachine extends TileMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileTripleGearMachine() {
        super(EnumMultiMachine.TRIPLE_Gearing.usagePerTick, EnumMultiMachine.TRIPLE_Gearing.lenghtOperation);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.075));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine3.triplegearing;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_Gearing;
    }

}
