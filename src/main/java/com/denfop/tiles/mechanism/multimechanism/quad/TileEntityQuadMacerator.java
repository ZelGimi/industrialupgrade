package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityQuadMacerator extends TileEntityMultiMachine {

    public TileEntityQuadMacerator() {
        super(
                EnumMultiMachine.QUAD_MACERATOR.usagePerTick,
                EnumMultiMachine.QUAD_MACERATOR.lenghtOperation,
                0
        );
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_MACERATOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockMacerator3.name");
    }

    public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
