package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityQuadAssamplerScrap extends TileEntityMultiMachine {

    public TileEntityQuadAssamplerScrap() {
        super(
                EnumMultiMachine.QUAD_AssamplerScrap.usagePerTick,
                EnumMultiMachine.QUAD_AssamplerScrap.lenghtOperation,
                3
        );
    }


    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_AssamplerScrap;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockAssamplerScrap3.name");
    }

    public String getStartSoundFile() {
        return "Machines/AssamplerScrap.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
