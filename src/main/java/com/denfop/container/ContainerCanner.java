package com.denfop.container;

import com.denfop.tiles.mechanism.TileCanner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerCanner extends ContainerFullInv<TileCanner> {

    public ContainerCanner(TileCanner tileEntity1, EntityPlayer player) {
        super(null, tileEntity1, 180);
        if (tileEntity1.outputSlot != null) {
            this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 119, 18));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 42, 18));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 1, 80, 45));

        for (int i = 0; i < 4; ++i) {
            this.addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 23 + i * 18));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.dischargeSlot, 0, 8, 77));
        final int width = 178;
        final int height = 180;
        int xStart = (width - 162) / 2;

        int col;
        for (col = 0; col < 3; ++col) {
            for (int col1 = 0; col1 < 9; ++col1) {
                this.addSlotToContainer(new Slot(
                        player.inventory,
                        col1 + col * 9 + 9,
                        xStart + col1 * 18,
                        height + 1 + -82 + col * 18
                ));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(player.inventory, col, xStart + col * 18, height + -24));
        }
    }


}
