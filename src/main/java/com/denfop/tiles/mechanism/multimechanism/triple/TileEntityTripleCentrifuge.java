package com.denfop.tiles.mechanism.multimechanism.triple;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;

public class TileEntityTripleCentrifuge extends TileEntityMultiMachine {

    public TileEntityTripleCentrifuge() {
        super(
                EnumMultiMachine.TRIPLE_Centrifuge.usagePerTick,
                EnumMultiMachine.TRIPLE_Centrifuge.lenghtOperation,
                4
        );
        this.cold.upgrade = true;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_Centrifuge;
    }

    public String getStartSoundFile() {
        return "Machines/centrifuge.ogg";
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
