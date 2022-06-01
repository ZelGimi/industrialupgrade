package com.denfop.tiles.mechanism;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityCombDoubleMacerator extends TileEntityMultiMachine {

    public TileEntityCombDoubleMacerator() {
        super(
                EnumMultiMachine.COMB_DOUBLE_MACERATOR.usagePerTick,
                EnumMultiMachine.COMB_DOUBLE_MACERATOR.lenghtOperation,
                1
        );
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.COMB_DOUBLE_MACERATOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCombMacerator1.name");
    }

    public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
