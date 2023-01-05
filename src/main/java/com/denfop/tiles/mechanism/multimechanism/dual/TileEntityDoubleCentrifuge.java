package com.denfop.tiles.mechanism.multimechanism.dual;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;

public class TileEntityDoubleCentrifuge extends TileEntityMultiMachine {

    public TileEntityDoubleCentrifuge() {
        super(
                EnumMultiMachine.DOUBLE_Centrifuge.usagePerTick,
                EnumMultiMachine.DOUBLE_Centrifuge.lenghtOperation,
                4
        );
        this.cold.upgrade = true;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_Centrifuge;
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
