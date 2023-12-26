package com.denfop.container;


import com.denfop.tiles.reactors.heat.graphite_controller.TileEntityGraphiteController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerHeatGraphiteController extends ContainerFullInv<TileEntityGraphiteController> {

    public ContainerHeatGraphiteController(TileEntityGraphiteController tileEntityExchanger, EntityPlayer var1) {
        super(var1,tileEntityExchanger,188,209);
        this.addSlotToContainer(new SlotInvSlot(tileEntityExchanger.getSlot(),0,84,58));
    }

}
