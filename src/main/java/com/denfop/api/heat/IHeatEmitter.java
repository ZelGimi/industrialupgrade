package com.denfop.api.heat;

import net.minecraft.util.EnumFacing;

public interface IHeatEmitter extends IHeatTile {

    boolean emitsHeatTo(IHeatAcceptor var1, EnumFacing var2);

}
