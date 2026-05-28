package com.denfop.containermenu;

import com.denfop.blockentity.storage.BlockEntityBus;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

public class ContainerBus extends ContainerMenuFullInv<BlockEntityBus> {


    public ContainerBus(Player entityPlayer, BlockEntityBus tileEntity1) {
        super(entityPlayer, tileEntity1, 171);
        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new SlotVirtual(
                    tileEntity1,
                    i,
                    62 + (i % 3) * 18,
                    23 + (i / 3) * 18,
                    tileEntity1.getListSlot()
            ));
        }
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickType, Player player) {
        if (clickType == ClickType.PICKUP_ALL)
            return;
        super.clicked(slotId, dragType, clickType, player);
    }

    protected void addPlayerInventorySlots(net.minecraft.world.entity.player.Inventory inventory, int width, int height) {
        int n4 = (width - 162) / 2;

        int n3;
        for (n3 = 0; n3 < 3; ++n3) {
            for (int i = 0; i < 9; ++i) {
                this.addSlot(new Slot(inventory, i + n3 * 9 + 9, n4 + i * 18, height + 1 + -82 + n3 * 18));
            }
        }

        for (n3 = 0; n3 < 9; ++n3) {
            this.addSlot(new Slot(inventory, n3, n4 + n3 * 18, height + -24));
        }

    }


}
