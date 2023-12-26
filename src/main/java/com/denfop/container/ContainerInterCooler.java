package com.denfop.container;

import com.denfop.tiles.reactors.gas.intercooler.TileEntityBaseInterCooler;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerInterCooler extends ContainerFullInv<TileEntityBaseInterCooler> {

    public ContainerInterCooler(TileEntityBaseInterCooler tileEntityBaseInterCooler, EntityPlayer var1) {
        super(var1,tileEntityBaseInterCooler,188,209);
        this.addSlotToContainer(new SlotInvSlot(tileEntityBaseInterCooler.getSlot(),0,85,50));

    }

}
