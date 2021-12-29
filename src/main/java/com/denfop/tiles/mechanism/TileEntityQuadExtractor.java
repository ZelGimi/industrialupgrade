package com.denfop.tiles.mechanism;


import com.denfop.invslot.InvSlotProcessableMultiGeneric;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.recipe.Recipes;
import ic2.core.init.Localization;

public class TileEntityQuadExtractor extends TileEntityMultiMachine {

    public TileEntityQuadExtractor() {
        super(
                EnumMultiMachine.QUAD_EXTRACTOR.usagePerTick,
                EnumMultiMachine.QUAD_EXTRACTOR.lenghtOperation,
                Recipes.extractor,
                0
        );
        this.inputSlots = new InvSlotProcessableMultiGeneric(this, "input", sizeWorkingSlot, Recipes.extractor);
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_EXTRACTOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockExtractor3.name");
    }

    public String getStartSoundFile() {
        return "Machines/ExtractorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
