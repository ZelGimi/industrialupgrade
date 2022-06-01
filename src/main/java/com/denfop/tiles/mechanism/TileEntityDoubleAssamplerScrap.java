package com.denfop.tiles.mechanism;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityDoubleAssamplerScrap extends TileEntityMultiMachine {

    public TileEntityDoubleAssamplerScrap() {
        super(
                EnumMultiMachine.DOUBLE_AssamplerScrap.usagePerTick,
                EnumMultiMachine.DOUBLE_AssamplerScrap.lenghtOperation,
                3
        );
    }


    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_AssamplerScrap;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockAssamplerScrap1.name");
    }

    public String getStartSoundFile() {
        return "Machines/AssamplerScrap.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
