package com.denfop.api.energy;

import net.minecraft.core.Direction;

public interface IEnergyEmitter extends IEnergyTile {

    boolean emitsEnergyTo(IEnergyAcceptor var1, Direction var2);

}
