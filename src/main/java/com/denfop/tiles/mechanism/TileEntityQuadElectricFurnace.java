package com.denfop.tiles.mechanism;

import com.denfop.invslot.InvSlotProcessableMultiGeneric;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.recipe.Recipes;
import ic2.core.init.Localization;

public class TileEntityQuadElectricFurnace extends TileEntityMultiMachine {

    public TileEntityQuadElectricFurnace() {
        super(
                EnumMultiMachine.QUAD_ELECTRIC_FURNACE.usagePerTick,
                EnumMultiMachine.QUAD_ELECTRIC_FURNACE.lenghtOperation,
                null,
                0
        );
        this.inputSlots = new InvSlotProcessableMultiGeneric(this, "input", sizeWorkingSlot, Recipes.furnace);
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_ELECTRIC_FURNACE;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockElecFurnace3.name");
    }

    public String getStartSoundFile() {
        return "Machines/Electro Furnace/ElectroFurnaceLoop.ogg";
    }

    public String getInterruptSoundFile() {
        return null;
    }


}
