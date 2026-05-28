package com.denfop.containermenu;

import com.denfop.blockentity.storage.BlockEntityMonitor;
import com.denfop.utils.ModUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ContainerMonitor extends ContainerMenuFullInv<BlockEntityMonitor> {
    public boolean canShift = true;
    public boolean isActiveAllInventory = true;

    public ContainerMonitor(BlockEntityMonitor tileEntity1, Player entityPlayer) {
        super(entityPlayer, tileEntity1, 214, 218, false);
        this.addPlayerInventorySlots(player.getInventory(), 214, 218);
    }

    protected void addPlayerInventorySlots(net.minecraft.world.entity.player.Inventory inventory, int width, int height) {
        int n4 = (width - 162) / 2;

        int n3;
        for (n3 = 0; n3 < 3; ++n3) {
            for (int i = 0; i < 9; ++i) {
                this.addSlot(new Slot(inventory, i + n3 * 9 + 9, n4 + i * 18, height + -80 + n3 * 18) {
                    @Override
                    public boolean isActive() {
                        return base.sizeMode == 0 && isActiveAllInventory;
                    }
                });
            }
        }

        for (n3 = 0; n3 < 9; ++n3) {
            this.addSlot(new Slot(inventory, n3, n4 + n3 * 18, height + -24) {
                @Override
                public boolean isActive() {
                    return base.sizeMode == 0 && isActiveAllInventory;
                }
            });
        }


        for (n3 = 0; n3 < 3; ++n3) {
            for (int i = 0; i < 9; ++i) {
                this.addSlot(new Slot(inventory, i + n3 * 9 + 9, n4 + i * 18, height + 38 + -82 + n3 * 18) {
                    @Override
                    public boolean isActive() {
                        return base.sizeMode == 1 && isActiveAllInventory;
                    }
                });
            }
        }

        for (n3 = 0; n3 < 9; ++n3) {
            this.addSlot(new Slot(inventory, n3, n4 + n3 * 18, height + 36 + -24) {
                @Override
                public boolean isActive() {
                    return base.sizeMode == 1 && isActiveAllInventory;
                }
            });
        }

        for (n3 = 0; n3 < 3; ++n3) {
            for (int i = 0; i < 9; ++i) {
                this.addSlot(new Slot(inventory, i + n3 * 9 + 9, n4 + i * 18, height - 52 + -82 + n3 * 18) {
                    @Override
                    public boolean isActive() {
                        return base.sizeMode == 2 && isActiveAllInventory;
                    }
                });
            }
        }

        for (n3 = 0; n3 < 9; ++n3) {
            this.addSlot(new Slot(inventory, n3, n4 + n3 * 18, height - 54 + -24) {
                @Override
                public boolean isActive() {
                    return base.sizeMode == 2 && isActiveAllInventory;
                }
            });
        }
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if (this.base.network != null)
            this.base.network.canUpdate = true;
        this.base.checkField = false;
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
