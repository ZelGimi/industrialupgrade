package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityStampMechanism;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerStamp extends ContainerFullInv<TileEntityStampMechanism> {

    public ContainerStamp(TileEntityStampMechanism tleEntityMatterFactory, EntityPlayer var1) {
        super(tleEntityMatterFactory, var1);
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.inputSlotA, 0, 30, 22));
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.inputSlotA, 1, 48, 22));
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.inputSlotA, 2, 30, 40));
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.inputSlotA, 3, 48, 40));
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.inputSlotB, 0, 10, 60));
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.outputSlot, 0, 100, 32));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.upgradeSlot, i, 152, 8 + i * 18));
        }
    }

}
