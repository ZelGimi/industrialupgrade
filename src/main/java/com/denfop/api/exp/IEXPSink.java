package com.denfop.api.exp;

import net.minecraft.util.EnumFacing;

public interface IEXPSink extends IEXPAcceptor {

    double getDemandedEXP();

    double injectEXP(EnumFacing var1, double var2, double var4);

}
