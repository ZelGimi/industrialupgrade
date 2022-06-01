package com.denfop.api.heat;

import net.minecraft.util.EnumFacing;

public interface IHeatSink extends IHeatAcceptor {

    double getDemandedHeat();

    double injectHeat(EnumFacing var1, double var2, double var4);

}
