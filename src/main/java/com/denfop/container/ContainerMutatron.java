package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityMutatron;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerMutatron extends ContainerFullInv<TileEntityMutatron> {

    public ContainerMutatron(EntityPlayer entityPlayer, TileEntityMutatron tileEntity1) {
        super(entityPlayer, tileEntity1, 206);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 45 - 8, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 1, 68 - 8, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 2, 122, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot1, 0, 45 - 8, 79));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot2, 0, 68 - 8, 79));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot3, 0, 122, 79));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 21 + i * 18
            ));
        }

    }


}
