package com.denfop.container;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.componets.AbstractComponent;
import com.denfop.invslot.Inventory;
import com.denfop.items.ItemStackInventory;
import com.denfop.network.packet.PacketUpdateFieldContainerItemStack;
import com.denfop.network.packet.PacketUpdateFieldContainerTile;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public abstract class ContainerBase<T extends IAdvInventory> extends Container {


    public final T base;

    public ContainerBase(T base1) {
        this.base = base1;
    }

    protected static boolean isValidTargetSlot(Slot slot, ItemStack stack, boolean allowEmpty, boolean requireInputOnly) {

        if (!slot.isItemValid(stack)) {
            return false;
        } else if (!allowEmpty && !slot.getHasStack()) {
            return false;
        } else if (!requireInputOnly) {
            return true;
        } else {
            return slot instanceof SlotInvSlot && ((SlotInvSlot) slot).inventory.canInput();
        }

    }

    public SlotInvSlot findClassSlot(Class<? extends Inventory> invSlotClass) {
        for (Slot slot : this.inventorySlots) {
            if (slot instanceof SlotInvSlot) {
                if (((SlotInvSlot) slot).inventory.getClass().equals(invSlotClass)) {
                    return (SlotInvSlot) slot;
                }
            }
        }
        return null;
    }

    public List<SlotInvSlot> getSlots() {
        List<SlotInvSlot> list = new ArrayList<>();
        for (Slot slot : this.inventorySlots) {
            if (slot instanceof SlotInvSlot) {
                list.add((SlotInvSlot) slot);
            }
        }
        return list;
    }

    public List<SlotInvSlot> findClassSlots(Class<? extends Inventory> invSlotClass) {
        List<SlotInvSlot> list = new ArrayList<>();
        for (Slot slot : this.inventorySlots) {
            if (slot instanceof SlotInvSlot) {
                if (((SlotInvSlot) slot).inventory.getClass().equals(invSlotClass)) {
                    list.add((SlotInvSlot) slot);
                }
            }
        }
        return list;
    }

    protected void addPlayerInventorySlots(EntityPlayer player, int height) {
        if (player != null) {
            this.addPlayerInventorySlots(player, 178, height);
        }
    }

    protected void addPlayerInventorySlots(EntityPlayer player, int width, int height) {
        if (player != null) {
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

    public @NotNull ItemStack slotClick(int slotId, int dragType, @NotNull ClickType clickType, @NotNull EntityPlayer player) {
        if (slotId < 0) {
            return super.slotClick(slotId, dragType, clickType, player);
        }
        Slot slot = this.inventorySlots.get(slotId);
        if (!(slot instanceof SlotVirtual)) {
            if (slot instanceof SlotInvSlot) {
                SlotInvSlot slot1 = (SlotInvSlot) slot;
                if (!slot1.inventory.canShift() && clickType == ClickType.QUICK_MOVE) {
                    return ItemStack.EMPTY;
                }
            }
            return super.slotClick(slotId, dragType, clickType, player);
        } else {
            ((SlotVirtual) slot).slotClick(slotId, dragType, clickType, player);
        }
        return ItemStack.EMPTY;
    }

    public final @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer player, int sourceSlotIndex) {
        Slot sourceSlot = this.inventorySlots.get(sourceSlotIndex);
        if (sourceSlot != null && sourceSlot.getHasStack()) {
            ItemStack sourceItemStack = sourceSlot.getStack();
            int oldSourceItemStackSize = ModUtils.getSize(sourceItemStack);
            ItemStack resultStack;
            if (sourceSlot.inventory == player.inventory) {
                resultStack = this.handlePlayerSlotShiftClick(player, sourceItemStack);
            } else {
                resultStack = this.handleGUISlotShiftClick(player, sourceItemStack);
            }

            if (ModUtils.isEmpty(resultStack) || ModUtils.getSize(resultStack) != oldSourceItemStackSize) {
                sourceSlot.putStack(resultStack);
                sourceSlot.onTake(player, sourceItemStack);
                if (!player.getEntityWorld().isRemote) {
                    this.detectAndSendChanges();
                }
            }
        }

        return ModUtils.emptyStack;
    }

    protected ItemStack handlePlayerSlotShiftClick(EntityPlayer player, ItemStack sourceItemStack) {
        for (int run = 0; run < 4 && !ModUtils.isEmpty(sourceItemStack); ++run) {

            for (final Slot targetSlot : this.inventorySlots) {
                if (targetSlot instanceof SlotVirtual) {
                    continue;
                }

                if (targetSlot.inventory != player.inventory && isValidTargetSlot(
                        targetSlot,
                        sourceItemStack,
                        run % 2 == 1,
                        run < 2
                )) {
                    sourceItemStack = this.transfer(sourceItemStack, targetSlot);
                    if (ModUtils.isEmpty(sourceItemStack)) {
                        break;
                    }
                }
            }
        }

        return sourceItemStack;
    }

    protected ItemStack handleGUISlotShiftClick(EntityPlayer player, ItemStack sourceItemStack) {
        for (int run = 0; run < 2 && !ModUtils.isEmpty(sourceItemStack); ++run) {
            ListIterator<Slot> it = this.inventorySlots.listIterator(this.inventorySlots.size());

            while (it.hasPrevious()) {
                Slot targetSlot = it.previous();
                if (targetSlot instanceof SlotVirtual) {
                    continue;
                }
                if (targetSlot.inventory == player.inventory && isValidTargetSlot(targetSlot, sourceItemStack, run == 1, false)) {
                    sourceItemStack = this.transfer(sourceItemStack, targetSlot);
                    if (ModUtils.isEmpty(sourceItemStack)) {
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
        if (this.base instanceof ItemStackInventory) {
            for (IContainerListener crafter : this.listeners) {
                if (crafter instanceof EntityPlayerMP) {
                    new PacketUpdateFieldContainerItemStack((ItemStackInventory) this.base, (EntityPlayerMP) crafter);
                }
            }
        }
        if (this.base instanceof TileEntity) {
            for (IContainerListener crafter : this.listeners) {
                if (crafter instanceof EntityPlayerMP) {
                    new PacketUpdateFieldContainerTile((TileEntityBlock) this.base, (EntityPlayerMP) crafter);
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


    protected final ItemStack transfer(ItemStack stack, Slot dst) {
        int amount = this.getTransferAmount(stack, dst);
        if (amount > 0) {
            ItemStack dstStack = dst.getStack();
            if (ModUtils.isEmpty(dstStack)) {
                dst.putStack(ModUtils.setSize(stack, amount));
            } else {
                dst.putStack(ModUtils.incSize(dstStack, amount));
            }

            stack = ModUtils.decSize(stack, amount);
        }
        return stack;
    }

    private int getTransferAmount(ItemStack stack, Slot dst) {
        int amount = Math.min(dst.inventory.getInventoryStackLimit(), dst.getSlotStackLimit());
        amount = Math.min(amount, stack.isStackable() ? stack.getMaxStackSize() : 1);
        ItemStack dstStack = dst.getStack();
        if (!ModUtils.isEmpty(dstStack)) {
            if (!ModUtils.checkItemEqualityStrict(stack, dstStack)) {
                return 0;
            }

            amount -= ModUtils.getSize(dstStack);
        }

        amount = Math.min(amount, ModUtils.getSize(stack));
        return amount;
    }


}
