package com.denfop.container;

import com.denfop.IUCore;
import com.denfop.componets.AbstractComponent;
import com.denfop.tiles.base.TileEntityInventory;
import ic2.core.slot.SlotHologramSlot;
import ic2.core.slot.SlotInvSlotReadOnly;
import ic2.core.util.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public abstract class ContainerBase<T extends IInventory> extends Container {

    protected static final int windowBorder = 8;
    protected static final int slotSize = 16;
    protected static final int slotDistance = 2;
    protected static final int slotSeparator = 4;
    protected static final int hotbarYOffset = -24;
    protected static final int inventoryYOffset = -82;
    public final T base;

    public ContainerBase(T base1) {
        this.base = base1;
    }

    protected static boolean isValidTargetSlot(Slot slot, ItemStack stack, boolean allowEmpty, boolean requireInputOnly) {
        if (!(slot instanceof SlotInvSlotReadOnly) && !(slot instanceof SlotHologramSlot)) {
            if (!slot.isItemValid(stack)) {
                return false;
            } else if (!allowEmpty && !slot.getHasStack()) {
                return false;
            } else if (!requireInputOnly) {
                return true;
            } else {
                return slot instanceof SlotInvSlot && ((SlotInvSlot) slot).invSlot.canInput();
            }
        } else {
            return false;
        }
    }

    protected void addPlayerInventorySlots(EntityPlayer player, int height) {
        this.addPlayerInventorySlots(player, 178, height);
    }

    protected void addPlayerInventorySlots(EntityPlayer player, int width, int height) {
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

    public @NotNull ItemStack slotClick(int slotId, int dragType, @NotNull ClickType clickType, @NotNull EntityPlayer player) {
        Slot slot;
        return slotId >= 0 && slotId < this.inventorySlots.size() && (slot = this.inventorySlots.get(slotId)) instanceof SlotHologramSlot
                ? ((SlotHologramSlot) slot).slotClick(dragType, clickType, player)
                : super.slotClick(slotId, dragType, clickType, player);
    }

    public final @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer player, int sourceSlotIndex) {
        Slot sourceSlot = this.inventorySlots.get(sourceSlotIndex);
        if (sourceSlot != null && sourceSlot.getHasStack()) {
            ItemStack sourceItemStack = sourceSlot.getStack();
            int oldSourceItemStackSize = StackUtil.getSize(sourceItemStack);
            ItemStack resultStack;
            if (sourceSlot.inventory == player.inventory) {
                resultStack = this.handlePlayerSlotShiftClick(player, sourceItemStack);
            } else {
                resultStack = this.handleGUISlotShiftClick(player, sourceItemStack);
            }

            if (StackUtil.isEmpty(resultStack) || StackUtil.getSize(resultStack) != oldSourceItemStackSize) {
                sourceSlot.putStack(resultStack);
                sourceSlot.onTake(player, sourceItemStack);
                if (!player.getEntityWorld().isRemote) {
                    this.detectAndSendChanges();
                }
            }
        }

        return StackUtil.emptyStack;
    }

    protected ItemStack handlePlayerSlotShiftClick(EntityPlayer player, ItemStack sourceItemStack) {
        for (int run = 0; run < 4 && !StackUtil.isEmpty(sourceItemStack); ++run) {

            for (final Slot targetSlot : this.inventorySlots) {
                if (targetSlot.inventory != player.inventory && isValidTargetSlot(
                        targetSlot,
                        sourceItemStack,
                        run % 2 == 1,
                        run < 2
                )) {
                    sourceItemStack = this.transfer(sourceItemStack, targetSlot);
                    if (StackUtil.isEmpty(sourceItemStack)) {
                        break;
                    }
                }
            }
        }

        return sourceItemStack;
    }

    protected ItemStack handleGUISlotShiftClick(EntityPlayer player, ItemStack sourceItemStack) {
        for (int run = 0; run < 2 && !StackUtil.isEmpty(sourceItemStack); ++run) {
            ListIterator<Slot> it = this.inventorySlots.listIterator(this.inventorySlots.size());

            while (it.hasPrevious()) {
                Slot targetSlot = it.previous();
                if (targetSlot.inventory == player.inventory && isValidTargetSlot(targetSlot, sourceItemStack, run == 1, false)) {
                    sourceItemStack = this.transfer(sourceItemStack, targetSlot);
                    if (StackUtil.isEmpty(sourceItemStack)) {
                        break;
                    }
                }
            }
        }

        return sourceItemStack;
    }

    public boolean canInteractWith(@NotNull EntityPlayer entityplayer) {
        return this.base.isUsableByPlayer(entityplayer);
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (this.base instanceof TileEntity) {
            for (final String name : this.getNetworkedFields()) {
                for (IContainerListener var5 : this.listeners) {
                    if (var5 instanceof EntityPlayerMP) {
                        IUCore.network.get(true).updateTileEntityFieldTo((TileEntity) this.base, name,
                                (EntityPlayerMP) var5
                        );
                    }
                }
            }

            if (this.base instanceof TileEntityInventory) {

                for (final AbstractComponent component : ((TileEntityInventory) this.base).getComponentList()) {
                    for (IContainerListener var5 : this.listeners) {
                        if (var5 instanceof EntityPlayerMP) {
                            component.onContainerUpdate((EntityPlayerMP) var5);
                        }
                    }
                }
            }
        }

    }

    public List<String> getNetworkedFields() {
        return new ArrayList<>();
    }

    public List<IContainerListener> getListeners() {
        return this.listeners;
    }

    public void onContainerEvent(String event) {
    }

    protected final ItemStack transfer(ItemStack stack, Slot dst) {
        int amount = this.getTransferAmount(stack, dst);
        if (amount > 0) {
            ItemStack dstStack = dst.getStack();
            if (StackUtil.isEmpty(dstStack)) {
                dst.putStack(StackUtil.copyWithSize(stack, amount));
            } else {
                dst.putStack(StackUtil.incSize(dstStack, amount));
            }

            stack = StackUtil.decSize(stack, amount);
        }
        return stack;
    }

    private int getTransferAmount(ItemStack stack, Slot dst) {
        int amount = Math.min(dst.inventory.getInventoryStackLimit(), dst.getSlotStackLimit());
        amount = Math.min(amount, stack.isStackable() ? stack.getMaxStackSize() : 1);
        ItemStack dstStack = dst.getStack();
        if (!StackUtil.isEmpty(dstStack)) {
            if (!StackUtil.checkItemEqualityStrict(stack, dstStack)) {
                return 0;
            }

            amount -= StackUtil.getSize(dstStack);
        }

        amount = Math.min(amount, StackUtil.getSize(stack));
        return amount;
    }

}
