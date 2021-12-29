package com.denfop.tiles.mechanism;

import com.denfop.invslot.InvSlotProcessableMultiGeneric;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.recipe.Recipes;
import ic2.core.init.Localization;

public class TileEntityDoubleExtruding extends TileEntityMultiMachine {

    public TileEntityDoubleExtruding() {
        super(
                EnumMultiMachine.DOUBLE_Extruding.usagePerTick,
                EnumMultiMachine.DOUBLE_Extruding.lenghtOperation,
                Recipes.metalformerExtruding,
                2
        );
        this.inputSlots = new InvSlotProcessableMultiGeneric(this, "input", sizeWorkingSlot, Recipes.metalformerExtruding);
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_Extruding;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockExtruding1.name");
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
