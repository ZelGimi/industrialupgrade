package com.denfop.container;

import com.denfop.tiles.base.TileBaseWorldCollector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerWorldCollector extends ContainerFullInv<TileBaseWorldCollector> {

    public ContainerWorldCollector(TileBaseWorldCollector tileEntity1, EntityPlayer entityPlayer) {
        super(null, tileEntity1, 166);


        addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlot,
                0, 44, 24
        ));


        addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                0, 110, 35
        ));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 9 + i * 18
            ));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.MatterSlot, 0, 44, 46));
        final int width = 178;
        final int height = 166;
        int xStart = (width - 162) / 2;

        int col;
        for (col = 0; col < 3; ++col) {
            for (int col1 = 0; col1 < 9; ++col1) {
                this.addSlotToContainer(new Slot(
                        entityPlayer.inventory,
                        col1 + col * 9 + 9,
                        xStart + col1 * 18,
                        height + -81 + col * 18
                ));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(entityPlayer.inventory, col, xStart + col * 18, height + -24));
        }

    }


}
