package com.denfop.tiles.mechanism.multimechanism.triple;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;

public class TileEntityTripleOreWashing extends TileEntityMultiMachine {

    public TileEntityTripleOreWashing() {
        super(
                EnumMultiMachine.TRIPLE_OreWashing.usagePerTick,
                EnumMultiMachine.TRIPLE_OreWashing.lenghtOperation,
                4
        );
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_OreWashing;
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
