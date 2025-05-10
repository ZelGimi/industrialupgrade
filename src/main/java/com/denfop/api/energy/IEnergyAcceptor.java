package com.denfop.api.energy;

import net.minecraft.core.Direction;

public interface IEnergyAcceptor extends IEnergyTile {

    boolean acceptsEnergyFrom(IEnergyEmitter var1, Direction var2);
}
