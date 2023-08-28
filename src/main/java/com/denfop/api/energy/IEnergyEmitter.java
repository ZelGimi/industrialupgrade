package com.denfop.api.energy;

import net.minecraft.util.EnumFacing;

public interface IEnergyEmitter extends IEnergyTile {

    boolean emitsEnergyTo(IEnergyAcceptor var1, EnumFacing var2);

}
