package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;

public class TileEntityQuadGearMachine extends TileEntityMultiMachine {

    public TileEntityQuadGearMachine() {
        super(EnumMultiMachine.QUAD_Gearing.usagePerTick, EnumMultiMachine.QUAD_Gearing.lenghtOperation, 1);

    }


    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_Gearing;
    }

}
