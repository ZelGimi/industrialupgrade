package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityQuadExtruding extends TileEntityMultiMachine {

    public TileEntityQuadExtruding() {
        super(
                EnumMultiMachine.QUAD_Extruding.usagePerTick,
                EnumMultiMachine.QUAD_Extruding.lenghtOperation,
                2
        );
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_Extruding;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockExtruding3.name");
    }

    public String getStartSoundFile() {
        return "Machines/extruder.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
