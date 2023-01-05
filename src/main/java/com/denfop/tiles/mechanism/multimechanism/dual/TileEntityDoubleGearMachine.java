package com.denfop.tiles.mechanism.multimechanism.dual;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;

public class TileEntityDoubleGearMachine extends TileEntityMultiMachine {

    public TileEntityDoubleGearMachine() {
        super(EnumMultiMachine.DOUBLE_Gearing.usagePerTick, EnumMultiMachine.DOUBLE_Gearing.lenghtOperation, 1);

    }


    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.DOUBLE_Gearing;
    }

}
