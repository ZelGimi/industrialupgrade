package com.denfop.container;


import com.denfop.tiles.base.TileEntityDoubleMolecular;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

import java.util.List;

public class ContainerBaseDoubleMolecular extends ContainerBase<TileEntityDoubleMolecular> {

    public ContainerBaseDoubleMolecular(EntityPlayer entityPlayer, TileEntityDoubleMolecular tileEntity1) {
        super(tileEntity1);
        if (tileEntity1.inputSlot != null) {
            addSlotToContainer(
                    new SlotInvSlot(tileEntity1.inputSlot, 0, 11, 27));
        }
        if (tileEntity1.inputSlot != null) {
            addSlotToContainer(
                    new SlotInvSlot(tileEntity1.inputSlot, 1, 29, 27));
        }

        if (tileEntity1.outputSlot != null) {
            addSlotToContainer(
                    new SlotInvSlot(tileEntity1.outputSlot, 0, 20, 68));
        }

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

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("guiProgress");
        ret.add("queue");
        ret.add("redstoneMode");
        ret.add("energy");
        ret.add("perenergy");
        ret.add("differenceenergy");
        return ret;
    }

}
