package com.denfop.container;

import com.denfop.tiles.base.TileEntityCombinerSEGenerators;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCombinerSE extends ContainerFullInv<TileEntityCombinerSEGenerators> {

    public ContainerCombinerSE(EntityPlayer entityPlayer, TileEntityCombinerSEGenerators tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 8 + i * 18));
        }
        for (int i = 0; i < 9; i++) {
            int count = i / 3;
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlot, i, 12 + (i - (3 * count)) * 18, 16 + count * 18));

        }
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.input, i, 70, 8 + i * 18));
        }
        for (int i = 0; i < 9; i++) {
            int count = i / 3;
            addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, i, 91 + (i - (3 * count)) * 18, 16 + count * 18));

        }
    }


}
