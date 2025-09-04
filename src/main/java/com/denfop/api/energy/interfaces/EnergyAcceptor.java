package com.denfop.api.energy.interfaces;

import net.minecraft.core.Direction;

public interface EnergyAcceptor extends EnergyTile {

    boolean acceptsEnergyFrom(EnergyEmitter var1, Direction var2);
}
