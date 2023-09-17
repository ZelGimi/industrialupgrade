package com.denfop.container;

import com.denfop.tiles.mechanism.TileMagnet;
import com.denfop.tiles.transport.tiles.TileEntityMultiCable;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCable extends ContainerFullInv<TileEntityMultiCable> {

    public ContainerCable(EntityPlayer entityPlayer, TileEntityMultiCable tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

    }


}
