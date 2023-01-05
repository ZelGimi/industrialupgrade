package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityQuadRolling extends TileEntityMultiMachine {

    public TileEntityQuadRolling() {
        super(
                EnumMultiMachine.QUAD_Rolling.usagePerTick,
                EnumMultiMachine.QUAD_Rolling.lenghtOperation,
                2
        );
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_Rolling;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockRolling3.name");
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public String getStartSoundFile() {
        return "Machines/plate.ogg";
    }


}
