
package com.denfop.api.qe;

import net.minecraft.util.EnumFacing;

public interface IQEAcceptor extends IQETile {

    boolean acceptsQEFrom(IQEEmitter var1, EnumFacing var2);

}
