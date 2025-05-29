package com.denfop.api.cool;

import net.minecraft.core.Direction;

public interface ICoolAcceptor extends ICoolTile {

    boolean acceptsCoolFrom(ICoolEmitter var1, Direction var2);

}
