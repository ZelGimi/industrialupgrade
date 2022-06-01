package com.denfop.api.se;

import net.minecraft.util.EnumFacing;

public interface ISEAcceptor extends ISETile {

    boolean acceptsSEFrom(ISEEmitter var1, EnumFacing var2);

}
