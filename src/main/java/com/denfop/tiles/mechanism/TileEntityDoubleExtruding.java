package com.denfop.tiles.mechanism;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityDoubleExtruding extends TileEntityMultiMachine {

    public TileEntityDoubleExtruding() {
        super(
                EnumMultiMachine.DOUBLE_Extruding.usagePerTick,
                EnumMultiMachine.DOUBLE_Extruding.lenghtOperation,
                2
        );
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_Extruding;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockExtruding1.name");
    }

    public String getStartSoundFile() {
        return "Machines/extruder.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
