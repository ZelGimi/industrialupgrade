package com.denfop.tiles.mechanism.multimechanism.simple;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;

public class TileEntityOreWashing extends TileEntityMultiMachine {

    public TileEntityOreWashing() {
        super(
                EnumMultiMachine.OreWashing.usagePerTick,
                EnumMultiMachine.OreWashing.lenghtOperation,
                4
        );
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.OreWashing;
    }

    public String getStartSoundFile() {
        return "Machines/ore_washing.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


 /*   public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }
*/

}
