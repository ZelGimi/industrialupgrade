package com.denfop.tiles.mechanism;

import com.denfop.invslot.InvSlotProcessableMultiGeneric;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.recipe.Recipes;
import ic2.core.init.Localization;

public class TileEntityQuadExtruding extends TileEntityMultiMachine {

    public TileEntityQuadExtruding() {
        super(
                EnumMultiMachine.QUAD_Extruding.usagePerTick,
                EnumMultiMachine.QUAD_Extruding.lenghtOperation,
                Recipes.metalformerExtruding,
                2
        );
        this.inputSlots = new InvSlotProcessableMultiGeneric(this, "input", sizeWorkingSlot, Recipes.metalformerExtruding);
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_Extruding;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockExtruding3.name");
    }

    public String getStartSoundFile() {
        return "Machines/extruder.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
