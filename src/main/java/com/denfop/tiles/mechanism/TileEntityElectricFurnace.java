package com.denfop.tiles.mechanism;


import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityElectricFurnace extends TileEntityMultiMachine {

    public TileEntityElectricFurnace() {
        super(
                EnumMultiMachine.ELECTRIC_FURNACE.usagePerTick,
                EnumMultiMachine.ELECTRIC_FURNACE.lenghtOperation,
                4
        );
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.ELECTRIC_FURNACE;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockElecFurnace.name");
    }

    public String getStartSoundFile() {
        return "Machines/Electro Furnace/ElectroFurnaceLoop.ogg";
    }

    public String getInterruptSoundFile() {
        return null;
    }


}
