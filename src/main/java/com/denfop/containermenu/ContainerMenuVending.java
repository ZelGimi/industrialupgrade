package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.vending.BlockEntityBaseVending;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;

public class ContainerMenuVending extends ContainerMenuFullInv<BlockEntityBaseVending> {

    public ContainerMenuVending(BlockEntityBaseVending tileEntityBaseVending, Player var1, final boolean b) {
        super(var1, tileEntityBaseVending, b ? 255 : 166);
        int sizeWorkingSlot = tileEntityBaseVending.getStyle().ordinal() + 1;
        int startPosX = -sizeWorkingSlot * 10;
        int dop = 5 - startPosX;
        int endPosX = (46 - sizeWorkingSlot) * (sizeWorkingSlot - 1) - sizeWorkingSlot * 10;

        if (endPosX - startPosX != 140) {
            dop += (46 * (4 - sizeWorkingSlot) / 2);
        }

        for (int i = 0; i < sizeWorkingSlot; i++) {
            int xDisplayPosition1 = dop + (46 - sizeWorkingSlot) * i - sizeWorkingSlot * 10;

            addSlotToContainer(new SlotInvSlot(tileEntityBaseVending.inventoryBuy, i, xDisplayPosition1, 16));
            addSlotToContainer(new SlotInvSlot(tileEntityBaseVending.inventorySell, i, xDisplayPosition1, 60));
            if (b) {
                int xDisplayPosition2 = dop + (46 - sizeWorkingSlot) * i - sizeWorkingSlot * 10 + 20;

                addSlotToContainer(new SlotInvSlot(tileEntityBaseVending.inventoryBuyPrivate, i, xDisplayPosition2, 16));
                addSlotToContainer(new SlotInvSlot(tileEntityBaseVending.inventorySellPrivate, i, xDisplayPosition2, 60));
            }
        }
        if (b) {
            for (int i = 0; i < 18; i++) {
                addSlotToContainer(new SlotInvSlot(tileEntityBaseVending.inventoryInventoryInput, i, 7 + (i % 9) * 18,
                        85 + (i / 9) * 18
                ));

            }
            for (int i = 0; i < 18; i++) {
                addSlotToContainer(new SlotInvSlot(tileEntityBaseVending.output, i, 7 + (i % 9) * 18,
                        125 + (i / 9) * 18
                ));

            }
        }
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickType, Player player) {
        if (clickType == ClickType.QUICK_CRAFT)
            return;
        super.clicked(slotId, dragType, clickType, player);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        this.base.timer = 5;
    }

}
