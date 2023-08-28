package com.denfop.container;

import com.denfop.tiles.mechanism.TileAdvOilRefiner;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerAdvOilRefiner extends ContainerFullInv<TileAdvOilRefiner> {

    public ContainerAdvOilRefiner(EntityPlayer entityPlayer, TileAdvOilRefiner tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot[0], 0, 14, 63));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 76, 63));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 1, 108, 63));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.containerslot[0], 0, 54, 16));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.containerslot[1], 0, 130, 16));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(
                    new SlotInvSlot((tileEntity1).upgradeSlot, i, 152, 8 + i * 18));
        }

    }


}
