package com.denfop.tiles.mechanism;

import com.denfop.invslot.InvSlotProcessableMultiGeneric;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.recipe.Recipes;
import ic2.core.init.Localization;

public class TileEntityExtruding extends TileEntityMultiMachine {

    public TileEntityExtruding() {
        super(EnumMultiMachine.Extruding.usagePerTick, EnumMultiMachine.Rolling.lenghtOperation, Recipes.metalformerExtruding, 2);
        this.inputSlots = new InvSlotProcessableMultiGeneric(this, "input", sizeWorkingSlot, Recipes.metalformerExtruding);
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Extruding;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockExtruding.name");
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
