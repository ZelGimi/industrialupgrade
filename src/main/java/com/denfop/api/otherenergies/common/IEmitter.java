package com.denfop.api.otherenergies.common;

import net.minecraft.core.Direction;

public interface IEmitter extends ITile {

    boolean emitsTo(IAcceptor var1, Direction var2);

}
