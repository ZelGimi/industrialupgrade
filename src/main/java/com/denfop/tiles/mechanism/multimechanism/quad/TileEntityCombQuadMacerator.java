package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityCombQuadMacerator extends TileEntityMultiMachine {

    public TileEntityCombQuadMacerator() {
        super(
                EnumMultiMachine.COMB_QUAD_MACERATOR.usagePerTick,
                EnumMultiMachine.COMB_QUAD_MACERATOR.lenghtOperation,
                1
        );
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.COMB_QUAD_MACERATOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCombMacerator3.name");
    }

    public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
