package com.denfop.api.energy.interfaces;

import net.minecraft.core.Direction;

public interface EnergyEmitter extends EnergyTile {

    boolean emitsEnergyTo(EnergyAcceptor var1, Direction var2);

}
