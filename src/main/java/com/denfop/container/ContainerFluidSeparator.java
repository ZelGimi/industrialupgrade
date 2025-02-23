package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityFluidSeparator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerFluidSeparator extends ContainerFullInv<TileEntityFluidSeparator> {

    public ContainerFluidSeparator(EntityPlayer entityPlayer, TileEntityFluidSeparator tileEntity1) {
        super(entityPlayer, tileEntity1, 206);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 50, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 1, 98, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 2, 122, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot1, 0, 50, 79));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot2, 0, 98, 79));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot3, 0, 122, 79));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 21 + i * 18
            ));
        }

    }


}
