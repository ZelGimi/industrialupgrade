package com.denfop.tiles.mechanism;

import com.denfop.invslot.InvSlotProcessableMultiGeneric;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.recipe.Recipes;
import ic2.core.init.Localization;

public class TileEntityTripleExtruding extends TileEntityMultiMachine {

    public TileEntityTripleExtruding() {
        super(
                EnumMultiMachine.TRIPLE_Extruding.usagePerTick,
                EnumMultiMachine.TRIPLE_Extruding.lenghtOperation,
                Recipes.metalformerExtruding,
                2
        );
        this.inputSlots = new InvSlotProcessableMultiGeneric(this, "input", sizeWorkingSlot, Recipes.metalformerExtruding);
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_Extruding;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockExtruding2.name");
    }

    public String getStartSoundFile() {
        return "Machines/extruder.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
