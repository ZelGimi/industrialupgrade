package com.denfop.api.exp;

import net.minecraft.util.EnumFacing;

public interface IEXPAcceptor extends IEXPTile {

    boolean acceptsEXPFrom(IEXPEmitter var1, EnumFacing var2);

}
