package com.denfop.api.se;

import net.minecraft.util.EnumFacing;

public interface ISESink extends ISEAcceptor {

    double getDemandedSE();

    double injectSE(EnumFacing var1, double var2, double var4);

}
