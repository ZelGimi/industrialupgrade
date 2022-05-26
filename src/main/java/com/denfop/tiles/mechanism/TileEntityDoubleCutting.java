package com.denfop.tiles.mechanism;

import com.denfop.invslot.InvSlotProcessableMultiGeneric;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.recipe.Recipes;
import ic2.core.init.Localization;

public class TileEntityDoubleCutting extends TileEntityMultiMachine {

    public TileEntityDoubleCutting() {
        super(
                EnumMultiMachine.DOUBLE_Cutting.usagePerTick,
                EnumMultiMachine.DOUBLE_Cutting.lenghtOperation,
                Recipes.metalformerCutting,
                2
        );
        this.inputSlots = new InvSlotProcessableMultiGeneric(this, "input", sizeWorkingSlot, Recipes.metalformerCutting);
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_Cutting;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCutting1.name");
    }

    public String getStartSoundFile() {
        return "Machines/cutter.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
