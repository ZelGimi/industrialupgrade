package com.denfop.containermenu;

import com.denfop.blockentity.storage.BlockEntityFluidMonitor;
import com.denfop.network.packet.PacketAddStack;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.ModUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

public class ContainerFluidMonitor extends ContainerMenuFullInv<BlockEntityFluidMonitor> {
    public boolean canShift = true;
    public boolean isActiveAllInventory = true;

    public ContainerFluidMonitor(BlockEntityFluidMonitor tileEntity1, Player entityPlayer) {
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
            ItemStack carried = sourceSlot.getItem().copy();
            if (!FluidHandlerFix.hasFluidHandler(carried) || carried.getCount() > 1) {
                if (FluidHandlerFix.hasFluidHandler(carried) && carried.getCount() > 1) {
                    carried = carried.copy();
                    int amount = carried.getCount();
                    carried.setCount(1);
                    IFluidHandlerItem handler = FluidHandlerFix.getFluidHandler(carried);
                    @NotNull FluidStack drain = handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
                    if (drain == null || drain.isEmpty())
                        return ModUtils.emptyStack;
                    ;
                    FluidStack insert = drain.copy();
                    insert.setAmount(insert.getAmount() * amount);
                    int canAdd = this.base.network.canAdd(insert);
                    if (canAdd / amount == drain.getAmount()) {
                        drain = handler.drain(canAdd / amount, IFluidHandler.FluidAction.SIMULATE);
                        if (canAdd > 0 && drain != null && !drain.isEmpty()) {
                            insert.setAmount(canAdd);
                            handler.drain(canAdd / amount, IFluidHandler.FluidAction.EXECUTE);
                            carried.setCount(amount);
                            sourceSlot.set(carried);
                            new PacketAddStack(player, insert, base.pos);
                            if (!player.level().isClientSide) {
                                this.broadcastChanges();
                            }
                        }
                    }
                } else {
                    return ModUtils.emptyStack;
                }
            }

            IFluidHandlerItem handler = FluidHandlerFix.getFluidHandler(carried);
            @NotNull FluidStack drain = handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
            if (drain == null || drain.isEmpty())
                return ModUtils.emptyStack;
            ;
            FluidStack insert = drain.copy();
            int canAdd = this.base.network.canAdd(insert);
            drain = handler.drain(canAdd, IFluidHandler.FluidAction.SIMULATE);
            if (canAdd > 0 && drain != null && !drain.isEmpty()) {
                insert.setAmount(canAdd);
                handler.drain(canAdd, IFluidHandler.FluidAction.EXECUTE);
                sourceSlot.set(carried);
                new PacketAddStack(player, insert, base.pos);
                if (!player.level().isClientSide) {
                    this.broadcastChanges();
                }
            }
        }

        return ModUtils.emptyStack;

    }
}
