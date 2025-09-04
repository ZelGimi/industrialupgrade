package com.denfop.api.otherenergies.cool;

import net.minecraft.core.Direction;

public interface ICoolEmitter extends ICoolTile {

    boolean emitsCoolTo(ICoolAcceptor var1, Direction var2);

}
