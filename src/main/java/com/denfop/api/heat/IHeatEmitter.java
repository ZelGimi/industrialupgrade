package com.denfop.api.heat;

import net.minecraft.core.Direction;

public interface IHeatEmitter extends IHeatTile {

    boolean emitsHeatTo(IHeatAcceptor var1, Direction var2);

}
