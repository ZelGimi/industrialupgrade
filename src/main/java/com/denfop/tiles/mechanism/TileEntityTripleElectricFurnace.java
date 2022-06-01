package com.denfop.tiles.mechanism;


import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityTripleElectricFurnace extends TileEntityMultiMachine {

    public TileEntityTripleElectricFurnace() {
        super(
                EnumMultiMachine.TRIPLE_ELECTRIC_FURNACE.usagePerTick,
                EnumMultiMachine.TRIPLE_ELECTRIC_FURNACE.lenghtOperation,
                0
        );
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_ELECTRIC_FURNACE;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockElecFurnace2.name");
    }

    public String getStartSoundFile() {
        return "Machines/Electro Furnace/ElectroFurnaceLoop.ogg";
    }

    public String getInterruptSoundFile() {
        return null;
    }


}
