package com.denfop.tiles.mechanism.multimechanism.dual;


import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityDoubleExtractor extends TileEntityMultiMachine {

    public TileEntityDoubleExtractor() {
        super(
                EnumMultiMachine.DOUBLE_EXTRACTOR.usagePerTick,
                EnumMultiMachine.DOUBLE_EXTRACTOR.lenghtOperation,
                0
        );
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_EXTRACTOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockExtractor.name");
    }

    public String getStartSoundFile() {
        return "Machines/ExtractorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
