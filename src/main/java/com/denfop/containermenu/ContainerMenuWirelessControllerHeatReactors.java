package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntityWirelessHeatController;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuWirelessControllerHeatReactors extends ContainerMenuFullInv<BlockEntityWirelessHeatController> {

    public ContainerMenuWirelessControllerHeatReactors(
            BlockEntityWirelessHeatController tileEntityWirelessControllerReactors,
            Player var1
    ) {
        super(tileEntityWirelessControllerReactors, var1);
        for (int i = 0; i < 1; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityWirelessControllerReactors.invslot, i, 80, 60
            ));

        }
    }

}
