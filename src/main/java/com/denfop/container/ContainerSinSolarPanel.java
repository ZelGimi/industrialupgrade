package com.denfop.container;

import com.denfop.tiles.base.TileSintezator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSinSolarPanel extends ContainerFullInv<TileSintezator> {

    public final TileSintezator tileentity;

    public ContainerSinSolarPanel(EntityPlayer entityPlayer, TileSintezator tileEntity1) {
        super(entityPlayer, tileEntity1, 178, 174);
        this.tileentity = tileEntity1;

        for (int j = 0; j < 9; ++j) {

            this.addSlotToContainer(new SlotInvSlot(this.tileentity.inputslot, j, 9 + j * 18, 60));
        }
        for (int j = 0; j < 4; ++j) {

            this.addSlotToContainer(new SlotInvSlot(this.tileentity.inputslotA, j, 95 + j * 18, 16));
        }
    }


}
