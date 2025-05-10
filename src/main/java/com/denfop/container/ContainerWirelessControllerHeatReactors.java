package com.denfop.container;

import com.denfop.tiles.base.TileEntityWirelessHeatController;
import net.minecraft.world.entity.player.Player;

public class ContainerWirelessControllerHeatReactors extends ContainerFullInv<TileEntityWirelessHeatController> {

    public ContainerWirelessControllerHeatReactors(
            TileEntityWirelessHeatController tileEntityWirelessControllerReactors,
            Player var1
    ) {
        super(tileEntityWirelessControllerReactors, var1);
        for (int i = 0; i < 1; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityWirelessControllerReactors.invslot, i, 80, 60
            ));

        }
    }

}
