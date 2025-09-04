package com.denfop.api.otherenergies.pressure;

import net.minecraft.core.Direction;

public class PressurePath {


    final IPressureSink target;
    final Direction targetDirection;

    PressurePath(IPressureSink sink, Direction facing) {
        this.target = sink;
        this.targetDirection = facing;
    }

    public void tick(int tick, double adding) {

    }


}
