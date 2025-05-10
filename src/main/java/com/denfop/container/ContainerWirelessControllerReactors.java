package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityWirelessControllerReactors;
import net.minecraft.world.entity.player.Player;

public class ContainerWirelessControllerReactors extends ContainerFullInv<TileEntityWirelessControllerReactors> {

    public ContainerWirelessControllerReactors(
            TileEntityWirelessControllerReactors tileEntityWirelessControllerReactors,
            Player var1
    ) {
        super(tileEntityWirelessControllerReactors, var1);
        for (int i = 0; i < 12; i++) {
            addSlotToContainer(new SlotInvSlot(
                    tileEntityWirelessControllerReactors.invslot,
                    i,
                    10 + (i / 3) * 36,
                    28 + (i % 3) * 18
            ));

        }
    }

}
