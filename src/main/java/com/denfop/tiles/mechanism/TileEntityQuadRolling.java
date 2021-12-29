package com.denfop.tiles.mechanism;

import com.denfop.invslot.InvSlotProcessableMultiGeneric;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.recipe.Recipes;
import ic2.core.init.Localization;

public class TileEntityQuadRolling extends TileEntityMultiMachine {

    public TileEntityQuadRolling() {
        super(
                EnumMultiMachine.QUAD_Rolling.usagePerTick,
                EnumMultiMachine.QUAD_Rolling.lenghtOperation,
                Recipes.metalformerRolling,
                2
        );
        this.inputSlots = new InvSlotProcessableMultiGeneric(this, "input", sizeWorkingSlot, Recipes.metalformerRolling);
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_Rolling;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockRolling3.name");
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
