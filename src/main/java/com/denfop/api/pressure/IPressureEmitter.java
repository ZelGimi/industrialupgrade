package com.denfop.api.pressure;

import net.minecraft.core.Direction;

public interface IPressureEmitter extends IPressureTile {

    boolean emitsPressureTo(IPressureAcceptor var1, Direction var2);

}
