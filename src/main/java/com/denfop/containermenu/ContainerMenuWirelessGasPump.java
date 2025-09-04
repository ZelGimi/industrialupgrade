package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityWirelessGasPump;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuWirelessGasPump extends ContainerMenuFullInv<BlockEntityWirelessGasPump> {

    public ContainerMenuWirelessGasPump(BlockEntityWirelessGasPump tileEntityWirelessOilPump, Player var1) {
        super(tileEntityWirelessOilPump, var1);
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityWirelessOilPump.invslot,
                    i, 152, 8 + i * 18
            ));

        }
    }

}
