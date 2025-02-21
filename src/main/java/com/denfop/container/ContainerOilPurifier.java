package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityOilPurifier;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerOilPurifier extends ContainerFullInv<TileEntityOilPurifier> {

    public ContainerOilPurifier(EntityPlayer entityPlayer, TileEntityOilPurifier tileEntity1) {
        super(entityPlayer, tileEntity1, 206);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.output1, 0, 45, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 100, 40));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.output1, 1, 122, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot1, 0, 45, 79));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot2, 0, 122, 79));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 21 + i * 18
            ));
        }

    }


}
