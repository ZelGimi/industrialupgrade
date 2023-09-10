package com.denfop.container;

import com.denfop.tiles.base.TileMolecularTransformer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerBaseMolecular extends ContainerBase<TileMolecularTransformer> {

    public ContainerBaseMolecular(EntityPlayer entityPlayer, TileMolecularTransformer tileEntity1) {
        super(tileEntity1);

        addSlotToContainer(
                new SlotInvSlot(tileEntity1.inputSlot, 0, 20, 27));
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.outputSlot, 0, 20, 68));

        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(
                        new Slot(entityPlayer.inventory, k + i * 9 + 9, 18 + k * 21, 98 + i * 21));
            }
        }
        for (int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(entityPlayer.inventory, j, 18 + j * 21, 165));
        }
    }


}
