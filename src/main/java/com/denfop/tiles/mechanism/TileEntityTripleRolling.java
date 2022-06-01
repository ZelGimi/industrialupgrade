package com.denfop.tiles.mechanism;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityTripleRolling extends TileEntityMultiMachine {

    public TileEntityTripleRolling() {
        super(EnumMultiMachine.Rolling.usagePerTick, EnumMultiMachine.Rolling.lenghtOperation, 2);
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_Rolling;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockRolling2.name");
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public String getStartSoundFile() {
        return "Machines/plate.ogg";
    }


}
