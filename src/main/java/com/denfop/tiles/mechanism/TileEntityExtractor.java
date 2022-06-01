package com.denfop.tiles.mechanism;


import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityExtractor extends TileEntityMultiMachine {

    public TileEntityExtractor() {
        super(
                EnumMultiMachine.EXTRACTOR.usagePerTick,
                EnumMultiMachine.EXTRACTOR.lenghtOperation,
                4
        );
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.EXTRACTOR;
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
