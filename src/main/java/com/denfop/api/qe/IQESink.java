
package com.denfop.api.qe;

import net.minecraft.util.EnumFacing;

public interface IQESink extends IQEAcceptor {

    double getDemandedQE();

    double injectQE(EnumFacing var1, double var2, double var4);

}
