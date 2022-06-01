package com.denfop.tiles.mechanism;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityExtruding extends TileEntityMultiMachine {

    public TileEntityExtruding() {
        super(EnumMultiMachine.Extruding.usagePerTick, EnumMultiMachine.Rolling.lenghtOperation, 2);
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Extruding;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockExtruding.name");
    }

    public String getStartSoundFile() {
        return "Machines/extruder.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
