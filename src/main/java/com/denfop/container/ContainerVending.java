package com.denfop.container;

import com.denfop.tiles.mechanism.vending.TileEntityBaseVending;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ContainerVending extends ContainerFullInv<TileEntityBaseVending> {

    public ContainerVending(TileEntityBaseVending tileEntityBaseVending, EntityPlayer var1, final boolean b) {
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

            addSlotToContainer(new SlotInvSlot(tileEntityBaseVending.invSlotBuy, i, xDisplayPosition1, 16));
            addSlotToContainer(new SlotInvSlot(tileEntityBaseVending.invSlotSell, i, xDisplayPosition1, 60));
            if (b) {
                int xDisplayPosition2 = dop + (46 - sizeWorkingSlot) * i - sizeWorkingSlot * 10 + 20;

                addSlotToContainer(new SlotInvSlot(tileEntityBaseVending.invSlotBuyPrivate, i, xDisplayPosition2, 16));
                addSlotToContainer(new SlotInvSlot(tileEntityBaseVending.invSlotSellPrivate, i, xDisplayPosition2, 60));
            }
        }
        if (b) {
            for (int i = 0; i < 18; i++) {
                addSlotToContainer(new SlotInvSlot(tileEntityBaseVending.invSlotInventoryInput, i, 7 + (i % 9) * 18,
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
    public @NotNull ItemStack slotClick(
            final int slotId,
            final int dragType,
            @NotNull final ClickType clickType,
            @NotNull final EntityPlayer player
    ) {
        if (clickType == ClickType.QUICK_CRAFT)
            return ItemStack.EMPTY;

        return super.slotClick(slotId, dragType, clickType, player);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        this.base.timer = 5;
    }

}
