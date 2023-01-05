package com.denfop.tiles.mechanism.multimechanism.dual;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityDoubleRolling extends TileEntityMultiMachine {

    public TileEntityDoubleRolling() {
        super(EnumMultiMachine.Rolling.usagePerTick, EnumMultiMachine.Rolling.lenghtOperation, 2);
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_Rolling;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockRolling1.name");
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public String getStartSoundFile() {
        return "Machines/plate.ogg";
    }


}
