package com.denfop.tiles.mechanism;

import com.denfop.api.Recipes;
import com.denfop.invslot.InvSlotProcessableMultiGeneric;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityCombQuadMacerator extends TileEntityMultiMachine {

    public TileEntityCombQuadMacerator() {
        super(
                EnumMultiMachine.COMB_QUAD_MACERATOR.usagePerTick,
                EnumMultiMachine.COMB_QUAD_MACERATOR.lenghtOperation,
                Recipes.macerator,
                1
        );
        this.inputSlots = new InvSlotProcessableMultiGeneric(this, "input", sizeWorkingSlot, Recipes.macerator);
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.COMB_QUAD_MACERATOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCombMacerator3.name");
    }

    public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
