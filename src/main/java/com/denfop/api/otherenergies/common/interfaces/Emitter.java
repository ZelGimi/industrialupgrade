package com.denfop.api.otherenergies.common.interfaces;

import net.minecraft.core.Direction;

public interface Emitter extends Tile {

    boolean emitsTo(Acceptor var1, Direction var2);

}
