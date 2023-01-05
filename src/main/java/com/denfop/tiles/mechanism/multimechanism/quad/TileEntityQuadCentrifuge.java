package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;

public class TileEntityQuadCentrifuge extends TileEntityMultiMachine {

    public TileEntityQuadCentrifuge() {
        super(
                EnumMultiMachine.QUAD_Centrifuge.usagePerTick,
                EnumMultiMachine.QUAD_Centrifuge.lenghtOperation,
                4
        );
        this.cold.upgrade = true;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_Centrifuge;
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
