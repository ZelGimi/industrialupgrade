package com.denfop.tiles.mechanism.multimechanism.simple;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;

public class TileEntityCentrifuge extends TileEntityMultiMachine {

    public TileEntityCentrifuge() {
        super(
                EnumMultiMachine.Centrifuge.usagePerTick,
                EnumMultiMachine.Centrifuge.lenghtOperation,
                4
        );
        this.cold.upgrade = true;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Centrifuge;
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
