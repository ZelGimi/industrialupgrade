package com.denfop.container;

import com.denfop.tiles.base.TileEntityAutomaticMechanism;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerAutomaticMechanism extends ContainerBase<TileEntityAutomaticMechanism> {

    public ContainerAutomaticMechanism(TileEntityAutomaticMechanism tileEntityAutomaticMechanism, EntityPlayer player) {
        super(tileEntityAutomaticMechanism);
        for (int i = 0; i < 36; i++) {
            addSlotToContainer(new SlotVirtual(tileEntityAutomaticMechanism, i, 8 + (i % 6) * 18, 25 + (i / 6) * 18,
                    tileEntityAutomaticMechanism.slot
            ));
        }
        for (int i = 0; i < 24; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityAutomaticMechanism.slotOI, i, 134 + (i % 4) * 18, 25 + (i / 4) * 18
            ));
        }
        final int width = 214;
        final int height = 243;
        int xStart = (width - 162) / 2;

        int col;
        for (col = 0; col < 3; ++col) {
            for (int col1 = 0; col1 < 9; ++col1) {
                this.addSlotToContainer(new Slot(
                        player.inventory,
                        col1 + col * 9 + 9,
                        xStart + col1 * 18,
                        height + -82 + col * 18
                ));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(player.inventory, col, xStart + col * 18, height + -24));
        }

    }

}
