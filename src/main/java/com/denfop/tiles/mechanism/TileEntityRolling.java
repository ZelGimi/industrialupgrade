package com.denfop.tiles.mechanism;

import com.denfop.invslot.InvSlotProcessableMultiGeneric;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.recipe.Recipes;
import ic2.core.init.Localization;

public class TileEntityRolling extends TileEntityMultiMachine {

    public TileEntityRolling() {
        super(EnumMultiMachine.Rolling.usagePerTick, EnumMultiMachine.Rolling.lenghtOperation, Recipes.metalformerRolling, 2);
        this.inputSlots = new InvSlotProcessableMultiGeneric(this, "input", sizeWorkingSlot, Recipes.metalformerRolling);
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Rolling;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockRolling.name");
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public String getStartSoundFile() {
        return "Machines/plate.ogg";
    }

}
