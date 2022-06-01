package com.denfop.api.se;

import net.minecraft.util.EnumFacing;

public interface ISEEmitter extends ISETile {

    boolean emitsSETo(ISEAcceptor var1, EnumFacing var2);

}
