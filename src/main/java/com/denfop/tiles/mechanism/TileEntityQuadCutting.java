package com.denfop.tiles.mechanism;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityQuadCutting extends TileEntityMultiMachine {

    public TileEntityQuadCutting() {
        super(EnumMultiMachine.QUAD_Cutting.usagePerTick, EnumMultiMachine.QUAD_Cutting.lenghtOperation,
                2
        );
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_Cutting;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCutting3.name");
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public String getStartSoundFile() {
        return "Machines/cutter.ogg";
    }

}
