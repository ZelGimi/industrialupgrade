package com.denfop.tiles.mechanism.multimechanism.triple;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityTripleMacerator extends TileEntityMultiMachine {

    public TileEntityTripleMacerator() {
        super(
                EnumMultiMachine.TRIPLE_MACERATOR.usagePerTick,
                EnumMultiMachine.TRIPLE_MACERATOR.lenghtOperation,
                0
        );
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_MACERATOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockMacerator2.name");
    }

    public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
