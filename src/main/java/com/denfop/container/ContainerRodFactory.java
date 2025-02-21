package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityRodFactory;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerRodFactory extends ContainerFullInv<TileEntityRodFactory> {

    public ContainerRodFactory(TileEntityRodFactory tileEntityRodFactory, EntityPlayer var1) {
        super(var1, tileEntityRodFactory);
        if (tileEntityRodFactory.type == 0) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityRodFactory.inputSlotA, 0, 30, 18));
            this.addSlotToContainer(new SlotInvSlot(tileEntityRodFactory.inputSlotA, 1, 12, 36));
            this.addSlotToContainer(new SlotInvSlot(tileEntityRodFactory.inputSlotA, 2, 30, 36));
            this.addSlotToContainer(new SlotInvSlot(tileEntityRodFactory.inputSlotA, 3, 48, 36));
            this.addSlotToContainer(new SlotInvSlot(tileEntityRodFactory.inputSlotA, 4, 30, 54));
        }
        if (tileEntityRodFactory.type == 1) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityRodFactory.inputSlotA, 0, 12, 36));
            this.addSlotToContainer(new SlotInvSlot(tileEntityRodFactory.inputSlotA, 1, 30, 36));
            this.addSlotToContainer(new SlotInvSlot(tileEntityRodFactory.inputSlotA, 2, 48, 36));
        }
        if (tileEntityRodFactory.type == 2) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityRodFactory.inputSlotA, 0, 12, 18));
            this.addSlotToContainer(new SlotInvSlot(tileEntityRodFactory.inputSlotA, 1, 30, 18));
            this.addSlotToContainer(new SlotInvSlot(tileEntityRodFactory.inputSlotA, 2, 48, 18));
            this.addSlotToContainer(new SlotInvSlot(tileEntityRodFactory.inputSlotA, 3, 30, 36));
            this.addSlotToContainer(new SlotInvSlot(tileEntityRodFactory.inputSlotA, 4, 12, 54));
            this.addSlotToContainer(new SlotInvSlot(tileEntityRodFactory.inputSlotA, 5, 30, 54));
            this.addSlotToContainer(new SlotInvSlot(tileEntityRodFactory.inputSlotA, 6, 48, 54));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntityRodFactory.outputSlot, 0, 100, 36));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityRodFactory.upgradeSlot, i, 152, 8 + i * 18));
        }
    }

}
