package com.denfop.tiles.mechanism;

import com.denfop.invslot.InvSlotProcessableMultiGeneric;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.recipe.Recipes;
import ic2.core.init.Localization;

public class TileEntityDoubleCombRecycler extends TileEntityMultiMachine {

    public TileEntityDoubleCombRecycler() {
        super(
                EnumMultiMachine.DOUBLE_COMB_RECYCLER.usagePerTick,
                EnumMultiMachine.DOUBLE_COMB_RECYCLER.lenghtOperation,
                Recipes.recycler,
                1
        );
        this.inputSlots = new InvSlotProcessableMultiGeneric(this, "input", 2, Recipes.recycler);
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_COMB_RECYCLER;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCombRecycler.name");
    }

    public String getStartSoundFile() {
        return "Machines/RecyclerOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
