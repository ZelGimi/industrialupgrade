package com.denfop.container;

import com.denfop.container.ContainerBase;
import com.denfop.tiles.reactors.graphite.capacitor.TileEntityCapacitor;
import com.denfop.tiles.reactors.graphite.exchanger.TileEntityExchanger;
import com.denfop.tiles.reactors.graphite.graphite_controller.TileEntityGraphiteController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGraphiteController extends ContainerFullInv<TileEntityGraphiteController> {

    public ContainerGraphiteController(TileEntityGraphiteController tileEntityExchanger, EntityPlayer var1) {
        super(var1,tileEntityExchanger,188,235);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(),0,84,58));
    }

}
