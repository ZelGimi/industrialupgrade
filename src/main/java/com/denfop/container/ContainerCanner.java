package com.denfop.container;

import com.denfop.tiles.mechanism.TileCanner;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCanner extends ContainerFullInv<TileCanner> {

    public ContainerCanner(TileCanner tileEntity1, EntityPlayer player) {
        super(player, tileEntity1, 184);
        if (tileEntity1.outputSlot != null) {
            this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 119, 17));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 41, 17));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 1, 80, 44));

        for (int i = 0; i < 4; ++i) {
            this.addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 26 + i * 18));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.dischargeSlot, 0, 8, 80));

    }


}
