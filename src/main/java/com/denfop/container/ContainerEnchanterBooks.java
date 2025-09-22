package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityEnchanterBooks;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerEnchanterBooks extends ContainerFullInv<TileEntityEnchanterBooks> {

    public ContainerEnchanterBooks(TileEntityEnchanterBooks tileEntityEnchanterBooks, EntityPlayer var1) {
        super(tileEntityEnchanterBooks, var1);
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 0, 20, 35));
        this.addSlotToContainer(new SlotInvSlot(base.inputSlotA, 1, 50, 35));
        this.addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 100, 35));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(base.upgradeSlot, i, 152, 8 + i * 18));
        }
    }

}
