package com.denfop.container;

import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.world.entity.player.Player;

public abstract class ContainerElectricMachine<T extends TileElectricMachine> extends ContainerFullInv<T> {

    public ContainerElectricMachine(Player player, T base1, int height, int dischargeX, int dischargeY) {
        super(player, base1, height);
        if (base1.dischargeSlot != null)
            this.addSlotToContainer(new SlotInvSlot(base1.dischargeSlot, 0, dischargeX, dischargeY));
    }

}
