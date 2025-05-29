package com.denfop.api.heat;

import net.minecraft.core.Direction;

public interface IHeatAcceptor extends IHeatTile {

    boolean acceptsHeatFrom(IHeatEmitter var1, Direction var2);

}
