package com.denfop.api.sytem;

import net.minecraft.util.EnumFacing;

public interface IAcceptor extends ITile {

    boolean acceptsFrom(IEmitter var1, EnumFacing var2);

}
