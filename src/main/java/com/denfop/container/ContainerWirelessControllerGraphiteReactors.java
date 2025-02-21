package com.denfop.container;

import com.denfop.tiles.base.TileEntityWirelessGraphiteController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerWirelessControllerGraphiteReactors extends ContainerFullInv<TileEntityWirelessGraphiteController> {

    public ContainerWirelessControllerGraphiteReactors(
            TileEntityWirelessGraphiteController tileEntityWirelessControllerReactors,
            EntityPlayer var1
    ) {
        super(tileEntityWirelessControllerReactors, var1);
        for (int i = 0; i < 1; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityWirelessControllerReactors.invslot, i, 80, 60
            ));

        }
    }

}
