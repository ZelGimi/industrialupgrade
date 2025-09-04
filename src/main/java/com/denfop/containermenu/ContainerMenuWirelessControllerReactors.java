package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityWirelessControllerReactors;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuWirelessControllerReactors extends ContainerMenuFullInv<BlockEntityWirelessControllerReactors> {

    public ContainerMenuWirelessControllerReactors(
            BlockEntityWirelessControllerReactors tileEntityWirelessControllerReactors,
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
