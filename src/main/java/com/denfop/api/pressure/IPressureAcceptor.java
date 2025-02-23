package com.denfop.api.pressure;

import net.minecraft.util.EnumFacing;

public interface IPressureAcceptor extends IPressureTile {

    boolean acceptsPressureFrom(IPressureEmitter var1, EnumFacing var2);

}
