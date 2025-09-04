package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntityElectricMachine;
import net.minecraft.world.entity.player.Player;

public abstract class ContainerMenuElectricMachine<T extends BlockEntityElectricMachine> extends ContainerMenuFullInv<T> {

    public ContainerMenuElectricMachine(Player player, T base1, int height, int dischargeX, int dischargeY) {
        super(player, base1, height);
        if (base1.dischargeSlot != null)
            this.addSlotToContainer(new SlotInvSlot(base1.dischargeSlot, 0, dischargeX, dischargeY));
    }

}
