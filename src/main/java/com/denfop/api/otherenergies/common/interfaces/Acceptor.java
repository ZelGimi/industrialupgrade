package com.denfop.api.otherenergies.common.interfaces;

import net.minecraft.core.Direction;

public interface Acceptor extends Tile {

    boolean acceptsFrom(Emitter var1, Direction var2);

}
