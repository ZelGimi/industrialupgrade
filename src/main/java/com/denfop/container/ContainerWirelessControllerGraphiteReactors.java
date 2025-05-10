package com.denfop.container;

import com.denfop.tiles.base.TileEntityWirelessGraphiteController;
import net.minecraft.world.entity.player.Player;

public class ContainerWirelessControllerGraphiteReactors extends ContainerFullInv<TileEntityWirelessGraphiteController> {

    public ContainerWirelessControllerGraphiteReactors(
            TileEntityWirelessGraphiteController tileEntityWirelessControllerReactors,
            Player var1
    ) {
        super(tileEntityWirelessControllerReactors, var1);
        for (int i = 0; i < 1; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityWirelessControllerReactors.invslot, i, 80, 60
            ));

        }
    }

}
