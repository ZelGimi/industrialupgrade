package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityInsulator;
import com.denfop.tiles.mechanism.TileEntityOilPurifier;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerInsulator extends ContainerFullInv<TileEntityInsulator> {

    public ContainerInsulator(EntityPlayer entityPlayer, TileEntityInsulator tileEntity1) {
        super(entityPlayer, tileEntity1, 206);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 25, 40));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.output1, 0, 47, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 120, 40));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.output1, 1, 67, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot1, 0, 47, 79));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot2, 0, 67, 79));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 21 + i * 18
            ));
        }

    }


}
