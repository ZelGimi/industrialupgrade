package com.denfop.tiles.mechanism;

import com.denfop.invslot.InvSlotProcessableMultiGeneric;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.recipe.Recipes;
import ic2.core.init.Localization;

public class TileEntityTripleCompressor extends TileEntityMultiMachine {

    public TileEntityTripleCompressor() {
        super(
                EnumMultiMachine.TRIPLE_COMPRESSER.usagePerTick,
                EnumMultiMachine.TRIPLE_COMPRESSER.lenghtOperation,
                Recipes.compressor,
                0
        );
        this.inputSlots = new InvSlotProcessableMultiGeneric(this, "input", sizeWorkingSlot, Recipes.compressor);
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_COMPRESSER;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCompressor2.name");
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
