package com.denfop.tiles.mechanism.multimechanism.triple;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;


public class TileEntityCombTripleMacerator extends TileEntityMultiMachine {

    public TileEntityCombTripleMacerator() {
        super(
                EnumMultiMachine.COMB_TRIPLE_MACERATOR.usagePerTick,
                EnumMultiMachine.COMB_TRIPLE_MACERATOR.lenghtOperation,
                1
        );
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.COMB_TRIPLE_MACERATOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCombMacerator2.name");
    }

    public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
