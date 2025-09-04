package com.denfop.api.otherenergies.common;

import net.minecraft.core.Direction;

public interface IAcceptor extends ITile {

    boolean acceptsFrom(IEmitter var1, Direction var2);

}
