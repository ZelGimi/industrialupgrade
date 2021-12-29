package com.denfop.tiles.mechanism;

import com.denfop.invslot.InvSlotProcessableMultiGeneric;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.recipe.Recipes;
import ic2.core.init.Localization;

public class TileEntityTripleCutting extends TileEntityMultiMachine {

    public TileEntityTripleCutting() {
        super(
                EnumMultiMachine.TRIPLE_Cutting.usagePerTick,
                EnumMultiMachine.TRIPLE_Cutting.lenghtOperation,
                Recipes.metalformerCutting,
                2
        );
        this.inputSlots = new InvSlotProcessableMultiGeneric(this, "input", sizeWorkingSlot, Recipes.metalformerCutting);
    }


    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_Cutting;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCutting2.name");
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
