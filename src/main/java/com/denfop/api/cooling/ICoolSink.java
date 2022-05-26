
package com.denfop.api.cooling;

import net.minecraft.util.EnumFacing;

public interface ICoolSink extends ICoolAcceptor {

    double getDemandedCool();

    double injectCool(EnumFacing var1, double var2, double var4);

}
