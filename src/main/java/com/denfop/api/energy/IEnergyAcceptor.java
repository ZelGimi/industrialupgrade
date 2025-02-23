package com.denfop.api.energy;

import net.minecraft.util.EnumFacing;

import java.util.List;

public interface IEnergyAcceptor extends IEnergyTile {

    boolean acceptsEnergyFrom(IEnergyEmitter var1, EnumFacing var2);
}
