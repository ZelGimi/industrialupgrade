
package com.denfop.api.cooling;

import net.minecraft.util.EnumFacing;

public interface ICoolAcceptor extends ICoolTile {

    boolean acceptsCoolFrom(ICoolEmitter var1, EnumFacing var2);

}
