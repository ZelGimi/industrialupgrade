package com.denfop.tiles.mechanism.multimechanism.dual;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;

public class TileEntityDoubleOreWashing extends TileEntityMultiMachine {

    public TileEntityDoubleOreWashing() {
        super(
                EnumMultiMachine.DOUBLE_OreWashing.usagePerTick,
                EnumMultiMachine.DOUBLE_OreWashing.lenghtOperation,
                4
        );
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_OreWashing;
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
