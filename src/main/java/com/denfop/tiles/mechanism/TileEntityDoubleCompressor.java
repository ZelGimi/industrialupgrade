package com.denfop.tiles.mechanism;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityDoubleCompressor extends TileEntityMultiMachine {

    public TileEntityDoubleCompressor() {
        super(
                EnumMultiMachine.DOUBLE_COMPRESSER.usagePerTick,
                EnumMultiMachine.DOUBLE_COMPRESSER.lenghtOperation,
                0
        );
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_COMPRESSER;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCompressor.name");
    }

    public String getStartSoundFile() {
        return "Machines/CompressorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
