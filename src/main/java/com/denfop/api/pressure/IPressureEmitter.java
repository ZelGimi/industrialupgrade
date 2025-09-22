package com.denfop.api.pressure;

import net.minecraft.util.EnumFacing;

public interface IPressureEmitter extends IPressureTile {

    boolean emitsPressureTo(IPressureAcceptor var1, EnumFacing var2);

}
