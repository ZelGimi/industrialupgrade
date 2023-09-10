package com.denfop.container;

import com.denfop.tiles.base.TileSintezator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSinSolarPanel extends ContainerFullInv<TileSintezator> {

    public final TileSintezator tileentity;

    public ContainerSinSolarPanel(EntityPlayer entityPlayer, TileSintezator tileEntity1) {
        super(entityPlayer, tileEntity1, 117 + 40 + 19 + 16 + 4, 186 - 18);
        this.tileentity = tileEntity1;

        for (int j = 0; j < 9; ++j) {

            this.addSlotToContainer(new SlotInvSlot(this.tileentity.inputslot, j, 17 + j * 18, 59));
        }
        for (int j = 0; j < 4; ++j) {

            this.addSlotToContainer(new SlotInvSlot(this.tileentity.inputslotA, j, 107 + j * 18, 28));
        }
    }


}
