package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityPump;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuPump extends ContainerMenuElectricMachine<BlockEntityPump> {

    public ContainerMenuPump(Player entityPlayer, BlockEntityPump tileEntity1) {
        super(entityPlayer, tileEntity1, 166, 8, 44);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.containerSlot, 0, 99, 17));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 132, 34));

        for (int i = 0; i < 4; ++i) {
            this.addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 8 + i * 18));
        }

    }


}
