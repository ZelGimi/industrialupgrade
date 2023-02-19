package com.denfop.api.sytem;

import net.minecraft.util.EnumFacing;

public interface IEmitter extends ITile {

    boolean emitsTo(IAcceptor var1, EnumFacing var2);

}
