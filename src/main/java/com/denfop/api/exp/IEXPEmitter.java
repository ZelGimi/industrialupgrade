package com.denfop.api.exp;

import net.minecraft.util.EnumFacing;

public interface IEXPEmitter extends IEXPTile {

    boolean emitsEXPTo(IEXPAcceptor var1, EnumFacing var2);

}
