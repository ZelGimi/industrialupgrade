package com.denfop.pressure;

import com.denfop.api.pressure.IPressureSink;
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
