package com.denfop.containermenu;

import com.denfop.IUCore;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.componets.AbstractComponent;
import com.denfop.inventory.Inventory;
import com.denfop.mixin.access.AbstractContainerMenuAccessor;
import com.denfop.network.packet.PacketUpdateFieldContainerTile;
import com.denfop.utils.ModUtils;
import com.google.common.base.Suppliers;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Supplier;

public class ContainerMenuBase<T extends CustomWorldContainer> extends AbstractContainerMenu {

    public final T base;
    public Player player;
    net.minecraft.world.entity.player.Inventory inventory;

    public ContainerMenuBase(T t, Player player) {
        super(t.getMenuType(), t.getContainerId());
        this.base = t;
        if (player != null) {
            this.player = player;
            this.inventory = player.getInventory();
        }
    }

    protected static final boolean isValidTargetSlot(Slot slot, ItemStack stack, boolean allowEmpty, boolean requireInputOnly) {
        if (!slot.mayPlace(stack)) {
            return false;
        } else if (!allowEmpty && !slot.hasItem()) {
            return false;
        } else if (!requireInputOnly) {
            return true;
        } else {
            return slot instanceof SlotInvSlot && ((SlotInvSlot) slot).inventory.canInput();
        }
    }

    public Player getPlayer() {
        return player;
    }

    protected Slot addSlotToContainer(Slot p_38898_) {
        return this.addSlot(p_38898_);
    }

    protected void addPlayerInventorySlots(net.minecraft.world.entity.player.Inventory inventory, int height) {
        this.addPlayerInventorySlots(inventory, 178, height);
        this.inventory = inventory;
    }

    public net.minecraft.world.entity.player.Inventory getInventory() {
        return inventory;
    }

    @Nullable
    public Slot getSlotFromInventory(Container inv, int slotIn) {
        for (Slot slot : this.slots) {
            if (slot.container == inv && slot.index == slotIn) {
                return slot;
            }
        }

        return slots.get(slotIn % slots.size());
    }

    protected void addPlayerInventorySlots(net.minecraft.world.entity.player.Inventory inventory, int width, int height) {
        int n4 = (width - 162) / 2;

        int n3;
        for (n3 = 0; n3 < 3; ++n3) {
            for (int i = 0; i < 9; ++i) {
                this.addSlot(new Slot(inventory, i + n3 * 9 + 9, n4 + i * 18, height + -82 + n3 * 18));
            }
        }

        for (n3 = 0; n3 < 9; ++n3) {
            this.addSlot(new Slot(inventory, n3, n4 + n3 * 18, height + -24));
        }

    }

    protected Slot addSlot(Slot p_38898_) {
        return super.addSlot(p_38898_);
    }

    public void clicked(int slotId, int dragType, ClickType clickType, Player player) {
        if (IUCore.network.getClient() != null && player.level().isClientSide) {
            if (checkClick()) {
                return;
            }
        }
        if (slotId < 0) {
            super.clicked(slotId, dragType, clickType, player);
            return;
        }
        Slot slot = this.slots.get(slotId);
        if (!(slot instanceof SlotVirtual)) {
            if (slot instanceof SlotInvSlot) {
                SlotInvSlot slot1 = (SlotInvSlot) slot;
                if (!slot1.inventory.canShift() && clickType == ClickType.QUICK_MOVE) {
                    return;
                }
                ((SlotInvSlot) slot).setDragType(dragType);
            }
            super.clicked(slotId, dragType, clickType, player);
            this.base.setChanged();
        } else {
            ((SlotVirtual) slot).slotClick(slotId, dragType, clickType, player);
        }


    }

    @OnlyIn(Dist.CLIENT)
    private boolean checkClick() {
        return Minecraft.getInstance().getSingleplayerServer() != null;
    }

    public final ItemStack quickMoveStack(Player player, int sourceSlotIndex) {
        Slot sourceSlot = this.slots.get(sourceSlotIndex);
        if (sourceSlot != null && sourceSlot.hasItem()) {
            ItemStack sourceItemStack = sourceSlot.getItem();
            int oldSourceItemStackSize = ModUtils.getSize(sourceItemStack);
            ItemStack resultStack;
            if (sourceSlot.container == player.getInventory()) {
                resultStack = this.handlePlayerSlotShiftClick(player, sourceItemStack);
            } else {
                resultStack = this.handleGUISlotShiftClick(player, sourceItemStack);
            }

            if (ModUtils.isEmpty(resultStack) || ModUtils.getSize(resultStack) != oldSourceItemStackSize) {
                sourceSlot.set(resultStack);
                sourceSlot.onTake(player, sourceItemStack);
                if (!player.level().isClientSide) {
                    this.broadcastChanges();
                }
            }
        }

        return ModUtils.emptyStack;

    }

    protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        return this.moveItemStackTo(stack, this.slots, startIndex, endIndex, reverseDirection);
    }

    protected boolean moveItemStackTo(ItemStack stack, List<Slot> slots, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = reverseDirection ? endIndex - 1 : startIndex;
        Slot slot;
        ItemStack itemstack;
        if (stack.isStackable()) {
            for (; !stack.isEmpty(); i += reverseDirection ? -1 : 1) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                slot = (Slot) slots.get(i);
                itemstack = slot.getItem();
                if (!itemstack.isEmpty() && slot.mayPlace(stack) && ItemStack.isSameItemSameTags(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = Math.min(slot.getMaxStackSize(itemstack), stack.getMaxStackSize());
                    if (j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.set(itemstack);
                        slot.setChanged();
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot.set(itemstack);
                        slot.setChanged();
                        flag = true;
                    }
                }
            }
        }

        if (!stack.isEmpty()) {
            i = reverseDirection ? endIndex - 1 : startIndex;

            while (true) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                slot = (Slot) slots.get(i);
                itemstack = slot.getItem();
                if (itemstack.isEmpty() && slot.mayPlace(stack)) {
                    slot.set(stack.split(Math.min(stack.getCount(), slot.getMaxStackSize(stack))));
                    slot.setChanged();
                    flag = true;
                    break;
                }

                i += reverseDirection ? -1 : 1;
            }
        }

        return flag;
    }

    protected ItemStack handlePlayerSlotShiftClick(Player player, ItemStack sourceItemStack) {
        for (int run = 0; run < 4 && !ModUtils.isEmpty(sourceItemStack); ++run) {

            for (final Slot targetSlot : this.slots) {
                if (targetSlot instanceof SlotVirtual) {
                    continue;
                }

                if (targetSlot.container != player.getInventory() && isValidTargetSlot(
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

    protected ItemStack handleGUISlotShiftClick(Player player, ItemStack sourceItemStack) {
        for (int run = 0; run < 2 && !sourceItemStack.isEmpty(); ++run) {
            ListIterator<Slot> it = this.slots.listIterator(this.slots.size());

            while (it.hasPrevious()) {
                Slot targetSlot = it.previous();
                if (targetSlot instanceof SlotVirtual) {
                    continue;
                }

                if (targetSlot.container == player.getInventory() && isValidTargetSlot(targetSlot, sourceItemStack, run == 1, false)) {
                    sourceItemStack = this.transfer(sourceItemStack, targetSlot);
                    if (sourceItemStack.isEmpty()) {
                        break;
                    }
                }
            }
        }

        return sourceItemStack;
    }

    public boolean stillValid(Player player) {
        return this.base.stillValid(player);
    }

    public void broadcastChanges() {
        for (int i = 0; i < this.slots.size(); ++i) {

            if (this.slots.get(i).container != player.getInventory() && slots.get(i) instanceof SlotInvSlot) {
                continue;
            }
            ItemStack itemstack = this.slots.get(i).getItem();

            Supplier<ItemStack> supplier = Suppliers.memoize(itemstack::copy);
            ((AbstractContainerMenuAccessor) this).invokeTriggerSlotListeners(i, itemstack, supplier);
            ((AbstractContainerMenuAccessor) this).invokeSynchronizeSlotToRemote(i, itemstack, supplier);
        }

        ((AbstractContainerMenuAccessor) this).invokeSynchronizeCarriedToRemote();
        if (this.base instanceof BlockEntity) {
            if (player instanceof ServerPlayer) {
                new PacketUpdateFieldContainerTile((BlockEntityBase) this.base, (ServerPlayer) player);
            }

            if (this.base instanceof BlockEntityInventory) {
                if (player instanceof ServerPlayer) {
                    for (final AbstractComponent component : ((BlockEntityInventory) this.base).getComponentList()) {
                        component.onContainerUpdate((ServerPlayer) player);
                    }
                }
            }

        }

    }


    protected final ItemStack transfer(ItemStack stack, Slot dst) {
        int amount = this.getTransferAmount(stack, dst);
        if (amount > 0) {
            ItemStack dstStack = dst.getItem();
            if (ModUtils.isEmpty(dstStack)) {
                dst.set(ModUtils.setSize(stack, amount));
            } else {
                dst.set(ModUtils.incSize(dstStack, amount));
            }

            stack = ModUtils.decSize(stack, amount);
        }
        return stack;
    }

    private int getTransferAmount(ItemStack stack, Slot dst) {
        int amount = Math.min(dst.container.getMaxStackSize(), dst.getMaxStackSize());
        amount = Math.min(amount, stack.isStackable() ? stack.getMaxStackSize() : 1);
        ItemStack dstStack = dst.getItem();
        if (!ModUtils.isEmpty(dstStack)) {
            if (!ModUtils.checkItemEqualityStrict(stack, dstStack)) {
                return 0;
            }

            amount -= ModUtils.getSize(dstStack);
        }

        amount = Math.min(amount, ModUtils.getSize(stack));
        return amount;
    }


    public SlotInvSlot findClassSlot(Class<? extends Inventory> invSlotClass) {
        for (Slot slot : this.slots) {
            if (slot instanceof SlotInvSlot) {
                if (((SlotInvSlot) slot).inventory.getClass().equals(invSlotClass)) {
                    return (SlotInvSlot) slot;
                }
            }
        }
        return null;
    }

    public List<SlotInvSlot> findClassSlots(Class<? extends Inventory> invSlotClass) {
        List<SlotInvSlot> list = new ArrayList<>();
        for (Slot slot : this.slots) {
            if (slot instanceof SlotInvSlot) {
                if (((SlotInvSlot) slot).inventory.getClass().equals(invSlotClass)) {
                    list.add((SlotInvSlot) slot);
                }
            }
        }
        return list;
    }


    public List<SlotInvSlot> getSlots() {
        List<SlotInvSlot> list = new ArrayList<>();
        for (Slot slot : this.slots) {
            if (slot instanceof SlotInvSlot) {
                list.add((SlotInvSlot) slot);
            }
        }
        return list;
    }
}
