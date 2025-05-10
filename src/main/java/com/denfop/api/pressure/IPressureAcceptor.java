package com.denfop.api.pressure;

import net.minecraft.core.Direction;

public interface IPressureAcceptor extends IPressureTile {

    boolean acceptsPressureFrom(IPressureEmitter var1, Direction var2);

}
