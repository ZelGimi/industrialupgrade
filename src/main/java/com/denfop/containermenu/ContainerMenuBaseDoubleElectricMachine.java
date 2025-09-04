package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntityDoubleElectricMachine;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public abstract class ContainerMenuBaseDoubleElectricMachine extends ContainerMenuFullInv<BlockEntityDoubleElectricMachine> {

    public ContainerMenuBaseDoubleElectricMachine(
            Player entityPlayer,
            BlockEntityDoubleElectricMachine base1,
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
