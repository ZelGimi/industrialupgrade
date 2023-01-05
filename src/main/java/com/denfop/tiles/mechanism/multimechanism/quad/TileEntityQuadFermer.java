package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityQuadFermer extends TileEntityMultiMachine {

    public TileEntityQuadFermer() {
        super(EnumMultiMachine.QUAD_Fermer.usagePerTick, EnumMultiMachine.QUAD_Fermer.lenghtOperation,
                3
        );
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_Fermer;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockFermer3.name");
    }

    public String getStartSoundFile() {
        return "Machines/Fermer.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
