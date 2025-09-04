package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityOilPump;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuOilPump extends ContainerMenuFullInv<BlockEntityOilPump> {

    public ContainerMenuOilPump(Player entityPlayer, BlockEntityOilPump tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 125, 59));
        addSlotToContainer(new SlotInvSlot(tileEntity1.containerslot, 0, 125, 23));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 8 + i * 18));
        }
    }


}
