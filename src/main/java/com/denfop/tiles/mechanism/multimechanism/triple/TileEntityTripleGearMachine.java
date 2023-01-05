package com.denfop.tiles.mechanism.multimechanism.triple;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;

public class TileEntityTripleGearMachine extends TileEntityMultiMachine {

    public TileEntityTripleGearMachine() {
        super(EnumMultiMachine.TRIPLE_Gearing.usagePerTick, EnumMultiMachine.TRIPLE_Gearing.lenghtOperation, 1);

    }


    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_Gearing;
    }

}
