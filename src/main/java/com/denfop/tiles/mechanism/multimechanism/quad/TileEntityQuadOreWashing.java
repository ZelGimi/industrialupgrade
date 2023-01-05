package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;

public class TileEntityQuadOreWashing extends TileEntityMultiMachine {

    public TileEntityQuadOreWashing() {
        super(
                EnumMultiMachine.QUAD_OreWashing.usagePerTick,
                EnumMultiMachine.QUAD_OreWashing.lenghtOperation,
                4
        );
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_OreWashing;
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
