package com.denfop.api.cool;

import net.minecraft.util.EnumFacing;

public interface ICoolEmitter extends ICoolTile {

    boolean emitsCoolTo(ICoolAcceptor var1, EnumFacing var2);

}
