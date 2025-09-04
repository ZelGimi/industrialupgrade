package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityEnchanterBooks;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuEnchanterBooks extends ContainerMenuFullInv<BlockEntityEnchanterBooks> {

    public ContainerMenuEnchanterBooks(BlockEntityEnchanterBooks tileEntityEnchanterBooks, Player var1) {
        super(tileEntityEnchanterBooks, var1);
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 0, 20, 35));
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 1, 50, 35));
        this.addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 100, 35));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(base.upgradeSlot, i, 152, 8 + i * 18));
        }
    }

}
