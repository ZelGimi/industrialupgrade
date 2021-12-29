package com.denfop.tiles.mechanism;

import com.denfop.invslot.InvSlotProcessableMultiGeneric;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.recipe.Recipes;
import ic2.core.init.Localization;

public class TileEntityTripleCombRecycler extends TileEntityMultiMachine {

    public TileEntityTripleCombRecycler() {
        super(
                EnumMultiMachine.TRIPLE_COMB_RRECYCLER.usagePerTick,
                EnumMultiMachine.TRIPLE_COMB_RRECYCLER.lenghtOperation,
                Recipes.recycler,
                1
        );
        this.inputSlots = new InvSlotProcessableMultiGeneric(this, "input", sizeWorkingSlot, Recipes.recycler);
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_COMB_RRECYCLER;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCombRecycler1.name");
    }

    public String getStartSoundFile() {
        return "Machines/RecyclerOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
