package com.denfop.tiles.mechanism;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityQuadCompressor extends TileEntityMultiMachine {

    public TileEntityQuadCompressor() {
        super(
                EnumMultiMachine.QUAD_COMPRESSER.usagePerTick,
                EnumMultiMachine.QUAD_COMPRESSER.lenghtOperation,
                0
        );
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_COMPRESSER;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCompressor3.name");
    }

    public String getStartSoundFile() {
        return "Machines/CompressorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }


}
