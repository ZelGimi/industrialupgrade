package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntityWirelessGraphiteController;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuWirelessControllerGraphiteReactors extends ContainerMenuFullInv<BlockEntityWirelessGraphiteController> {

    public ContainerMenuWirelessControllerGraphiteReactors(
            BlockEntityWirelessGraphiteController tileEntityWirelessControllerReactors,
            Player var1
    ) {
        super(tileEntityWirelessControllerReactors, var1);
        for (int i = 0; i < 1; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityWirelessControllerReactors.invslot, i, 80, 60
            ));

        }
    }

}
