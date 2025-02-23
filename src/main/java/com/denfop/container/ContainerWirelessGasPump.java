package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityWirelessGasPump;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerWirelessGasPump extends ContainerFullInv<TileEntityWirelessGasPump> {

    public ContainerWirelessGasPump(TileEntityWirelessGasPump tileEntityWirelessOilPump, EntityPlayer var1) {
        super(tileEntityWirelessOilPump, var1);
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityWirelessOilPump.invslot,
                    i, 152, 8 + i * 18
            ));

        }
    }

}
