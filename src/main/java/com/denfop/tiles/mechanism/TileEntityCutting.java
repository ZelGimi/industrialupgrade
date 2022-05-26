package com.denfop.tiles.mechanism;

import com.denfop.invslot.InvSlotProcessableMultiGeneric;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.recipe.Recipes;
import ic2.core.init.Localization;

public class TileEntityCutting extends TileEntityMultiMachine {

    public TileEntityCutting() {
        super(EnumMultiMachine.Cutting.usagePerTick, EnumMultiMachine.Cutting.lenghtOperation, Recipes.metalformerCutting, 2);
        this.inputSlots = new InvSlotProcessableMultiGeneric(this, "input", sizeWorkingSlot, Recipes.metalformerCutting);
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Cutting;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCutting.name");
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public String getStartSoundFile() {
        return "Machines/cutter.ogg";
    }


}
