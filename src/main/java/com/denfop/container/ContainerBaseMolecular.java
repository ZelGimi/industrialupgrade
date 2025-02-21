package com.denfop.container;

import com.denfop.tiles.base.TileMolecularTransformer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerBaseMolecular extends ContainerBase<TileMolecularTransformer> {

    public ContainerBaseMolecular(EntityPlayer entityPlayer, TileMolecularTransformer tileEntity1) {
        super(tileEntity1);
        if (tileEntity1.maxAmount == 1) {
            addSlotToContainer(
                    new SlotInvSlot(tileEntity1.inputSlot[0], 0, 22, 57));
            addSlotToContainer(
                    new SlotInvSlot(tileEntity1.outputSlot[0], 0, 22, 97));
        }else if (tileEntity1.maxAmount == 2){
            addSlotToContainer(
                    new SlotInvSlot(tileEntity1.inputSlot[0], 0, 25, 57));
            addSlotToContainer(
                    new SlotInvSlot(tileEntity1.outputSlot[0], 0, 25, 97));
            addSlotToContainer(
                    new SlotInvSlot(tileEntity1.inputSlot[1], 0, 45, 57));
            addSlotToContainer(
                    new SlotInvSlot(tileEntity1.outputSlot[1], 0, 45, 97));
        }else if (tileEntity1.maxAmount == 4){
            addSlotToContainer(
                    new SlotInvSlot(tileEntity1.inputSlot[0], 0, 9, 57));
            addSlotToContainer(
                    new SlotInvSlot(tileEntity1.outputSlot[0], 0, 9, 97));
            addSlotToContainer(
                    new SlotInvSlot(tileEntity1.inputSlot[1], 0, 28, 57));
            addSlotToContainer(
                    new SlotInvSlot(tileEntity1.outputSlot[1], 0, 28, 97));
            addSlotToContainer(
                    new SlotInvSlot(tileEntity1.inputSlot[2], 0, 47, 57));
            addSlotToContainer(
                    new SlotInvSlot(tileEntity1.outputSlot[2], 0, 47, 97));
            addSlotToContainer(
                    new SlotInvSlot(tileEntity1.inputSlot[3], 0, 66, 57));
            addSlotToContainer(
                    new SlotInvSlot(tileEntity1.outputSlot[3], 0, 66, 97));
        }
        int dopX =tileEntity1.maxAmount > 1 ? (tileEntity1.maxAmount == 2 ? 25/2 : 32 / 2) : 0;
        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(
                        new Slot(entityPlayer.inventory, k + i * 9 + 9, 20 + k * 21 +dopX, 128 + i * 21));
            }
        }
        for (int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(entityPlayer.inventory, j, 20 + j * 21 +dopX, 195));
        }
    }


}
