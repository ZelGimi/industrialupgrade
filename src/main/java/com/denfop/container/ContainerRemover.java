package com.denfop.container;

import com.denfop.tiles.mechanism.energy.TileEnergyRemover;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerRemover extends ContainerFullInv<TileEnergyRemover> {

    public ContainerRemover(TileEnergyRemover tileEntity1, EntityPlayer entityPlayer) {
        super(entityPlayer, tileEntity1, 202);
        for (int i = 0; i < 16; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.slot, i, 9 + (18 * (i % 4)), 17 + (18 * (i / 4))));

        }
    }


}
