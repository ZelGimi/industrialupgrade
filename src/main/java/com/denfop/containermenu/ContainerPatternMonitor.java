package com.denfop.containermenu;

import com.denfop.blockentity.storage.BlockEntityPatternMonitor;
import com.denfop.utils.ModUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ContainerPatternMonitor extends ContainerMenuFullInv<BlockEntityPatternMonitor> {
    public boolean canShift = true;
    public boolean isActiveAllInventory = true;

    public ContainerPatternMonitor(BlockEntityPatternMonitor tileEntity1, Player entityPlayer) {
        super(entityPlayer, tileEntity1, 214, 218, false);
        this.addPlayerInventorySlots(player.getInventory(), 214, 219 + 36);
        for (int i = 0; i < 36; i++) {

            int page = i / 9;
            int index = i % 9;

            int x = 44 + (index % 3) * 18;
            int y = 114 + (index / 3) * 18;

            addSlotToContainer(new SlotVirtualMonitor(tileEntity1, i, x, y, tileEntity1.inputItems) {

                @Override
                public boolean isActive() {
                    return isActiveAllInventory && page == tileEntity1.value;
                }
            });
        }
        for (int i = 0; i < 36; i++) {

            int page = i / 9;
            int index = i % 9;

            int x = 116 + (index % 3) * 18;
            int y = 114 + (index / 3) * 18;

            addSlotToContainer(new SlotVirtualMonitor(tileEntity1, i, x, y, tileEntity1.outputItems) {

                @Override
                public boolean isActive() {
                    return isActiveAllInventory && page == tileEntity1.value1;
                }
            });
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.patternItemOutput, 0, 189, 148) {
            @Override
            public boolean isActive() {
                return isActiveAllInventory;
            }
        });

        addSlotToContainer(new SlotInvSlot(tileEntity1.patternItem, 0, 189, 114
        ) {
            @Override
            public boolean isActive() {
                return isActiveAllInventory;
            }
        });
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if (this.base.network != null)
            this.base.network.canUpdate = true;
        this.base.checkField = false;
    }

    protected void addPlayerInventorySlots(net.minecraft.world.entity.player.Inventory inventory, int width, int height) {
        int n4 = (width - 162) / 2;

        int n3;
        for (n3 = 0; n3 < 3; ++n3) {
            for (int i = 0; i < 9; ++i) {
                this.addSlot(new Slot(inventory, i + n3 * 9 + 9, n4 + i * 18, height + -80 + n3 * 18) {
                    @Override
                    public boolean isActive() {
                        return isActiveAllInventory;
                    }
                });
            }
        }

        for (n3 = 0; n3 < 9; ++n3) {
            this.addSlot(new Slot(inventory, n3, n4 + n3 * 18, height + -24) {
                @Override
                public boolean isActive() {
                    return isActiveAllInventory;
                }
            });
        }


    }

    public ItemStack quickMoveStack(Player player, int sourceSlotIndex) {
        Slot sourceSlot = this.getSlot(sourceSlotIndex);
        if (sourceSlot != null && sourceSlot.hasItem() && canShift && this.base.network != null && this.base.network.hasFreeEnergy()) {
            ItemStack insert = sourceSlot.getItem().copy();
            int canAdd = this.base.network.canAdd(insert);
            if (canAdd > 0) {
                insert.setCount(canAdd);
                this.base.network.addStack(insert.getItem(), insert.getComponents(), insert.getCount());
                sourceSlot.set(insert.split(sourceSlot.getItem().getCount() - insert.getCount()));
                sourceSlot.onTake(player, sourceSlot.getItem());
                if (!player.level().isClientSide) {
                    this.broadcastChanges();
                }
            }
        }

        return ModUtils.emptyStack;

    }
}
