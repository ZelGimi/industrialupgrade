package com.denfop.api.heat;

import net.minecraft.util.EnumFacing;

public interface IHeatAcceptor extends IHeatTile {

    boolean acceptsHeatFrom(IHeatEmitter var1, EnumFacing var2);

}
