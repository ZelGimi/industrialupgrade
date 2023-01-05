package com.denfop.tiles.mechanism.multimechanism.triple;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityTripleExtruding extends TileEntityMultiMachine {

    public TileEntityTripleExtruding() {
        super(
                EnumMultiMachine.TRIPLE_Extruding.usagePerTick,
                EnumMultiMachine.TRIPLE_Extruding.lenghtOperation,
                2
        );
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_Extruding;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockExtruding2.name");
    }

    public String getStartSoundFile() {
        return "Machines/extruder.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
