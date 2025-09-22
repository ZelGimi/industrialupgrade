package com.denfop.container;

import com.denfop.tiles.reactors.graphite.graphite_controller.TileEntityGraphiteController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGraphiteController extends ContainerFullInv<TileEntityGraphiteController> {

    public ContainerGraphiteController(TileEntityGraphiteController tileEntityExchanger, EntityPlayer var1) {
        super(var1, tileEntityExchanger, 188, 208);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(), 0, 86, 70));
    }

}
