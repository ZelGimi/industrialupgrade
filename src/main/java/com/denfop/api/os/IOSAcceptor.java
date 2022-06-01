package com.denfop.api.os;

import net.minecraft.util.EnumFacing;

public interface IOSAcceptor extends IOSTile {

    boolean acceptsOSFrom(IOSEmitter var1, EnumFacing var2);

}
