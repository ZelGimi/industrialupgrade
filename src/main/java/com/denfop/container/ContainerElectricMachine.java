package com.denfop.container;

import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.entity.player.EntityPlayer;

public abstract class ContainerElectricMachine<T extends TileElectricMachine> extends ContainerFullInv<T> {

    public ContainerElectricMachine(EntityPlayer player, T base1, int height, int dischargeX, int dischargeY) {
        super(player, base1, height);
        this.addSlotToContainer(new SlotInvSlot(base1.dischargeSlot, 0, dischargeX, dischargeY));
    }

}
