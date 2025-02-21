package com.denfop.container;

import com.denfop.tiles.base.TileBaseObsidianGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerObsidianGenerator extends ContainerFullInv<TileBaseObsidianGenerator> {

    public ContainerObsidianGenerator(EntityPlayer entityPlayer, TileBaseObsidianGenerator tileEntity1) {
        super(null, tileEntity1, 166);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 127, 41));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot1, 0, 23, 59));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot1, 0, 23, 19));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot2, 0, 23, 39));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 9 + i * 18
            ));
        }
        final int width = 178;
        int height = 167;
        int xStart = (width - 162) / 2;

        int col;
        for (col = 0; col < 3; ++col) {
            for (int col1 = 0; col1 < 9; ++col1) {
                this.addSlotToContainer(new Slot(
                        entityPlayer.inventory,
                        col1 + col * 9 + 9,
                        xStart + col1 * 18,
                        height + -82 + col * 18
                ));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(entityPlayer.inventory, col, xStart + col * 18, -1 + height + -24));
        }
    }


}
