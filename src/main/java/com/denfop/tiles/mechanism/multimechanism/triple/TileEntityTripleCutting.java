package com.denfop.tiles.mechanism.multimechanism.triple;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityTripleCutting extends TileEntityMultiMachine {

    public TileEntityTripleCutting() {
        super(
                EnumMultiMachine.TRIPLE_Cutting.usagePerTick,
                EnumMultiMachine.TRIPLE_Cutting.lenghtOperation,
                2
        );
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_Cutting;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCutting2.name");
    }

    public String getStartSoundFile() {
        return "Machines/cutter.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
