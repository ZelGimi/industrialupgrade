package com.denfop.tiles.mechanism.multimechanism.simple;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileCentrifuge extends TileMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileCentrifuge() {
        super(
                EnumMultiMachine.Centrifuge.usagePerTick,
                EnumMultiMachine.Centrifuge.lenghtOperation
        );
        this.cold.upgrade = true;
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.15));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine3.centrifuge_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Centrifuge;
    }

    public String getStartSoundFile() {
        return "Machines/centrifuge.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


 /*   public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }
*/

}
