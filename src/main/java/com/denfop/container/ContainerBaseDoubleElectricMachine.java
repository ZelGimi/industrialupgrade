package com.denfop.container;

import com.denfop.tiles.base.TileDoubleElectricMachine;
import net.minecraft.entity.player.EntityPlayer;

public abstract class ContainerBaseDoubleElectricMachine extends ContainerFullInv<TileDoubleElectricMachine> {

    public ContainerBaseDoubleElectricMachine(
            EntityPlayer entityPlayer,
            TileDoubleElectricMachine base1,
            int height,
            int dischargeX,
            int dischargeY,
            boolean register
    ) {
        super(entityPlayer, base1, height);
        if (register) {
            this.addSlotToContainer(new SlotInvSlot(base1.dischargeSlot, 0, dischargeX, dischargeY));
        }
    }


}
