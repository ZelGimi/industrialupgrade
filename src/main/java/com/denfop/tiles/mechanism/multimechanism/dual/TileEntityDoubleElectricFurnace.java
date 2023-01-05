package com.denfop.tiles.mechanism.multimechanism.dual;


import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityDoubleElectricFurnace extends TileEntityMultiMachine {

    public TileEntityDoubleElectricFurnace() {
        super(
                EnumMultiMachine.DOUBLE_ELECTRIC_FURNACE.usagePerTick,
                EnumMultiMachine.DOUBLE_ELECTRIC_FURNACE.lenghtOperation,
                0
        );
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_ELECTRIC_FURNACE;
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
