package com.denfop.api.otherenergies.common;

import net.minecraft.core.Direction;

public class InfoTile<T> {


    public final T tileEntity;
    public final Direction direction;

    public InfoTile(final T tileEntity, final Direction direction) {
        this.tileEntity = tileEntity;
        this.direction = direction;
    }

}

