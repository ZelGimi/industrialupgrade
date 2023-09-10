package com.denfop.api.sytem;

import net.minecraft.util.EnumFacing;

public class InfoTile<T> {


    public final T tileEntity;
    public final EnumFacing direction;

    public InfoTile(final T tileEntity, final EnumFacing direction) {
        this.tileEntity = tileEntity;
        this.direction = direction;
    }

}

