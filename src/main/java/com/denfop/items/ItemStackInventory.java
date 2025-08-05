package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.inv.IStackInventory;
import com.denfop.container.ContainerBase;
import com.denfop.datacomponent.ContainerItem;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlot;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.IUpdatableItemStack;
import com.denfop.utils.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.denfop.register.Register.inventory_container;

public abstract class ItemStackInventory implements IAdvInventory<ItemStackInventory>, IUpdatableItemStack, IStackInventory {

    public final Player player;
    public final int inventorySize;
    public List<ItemStack> inventory;
    public ContainerItem containerItem;
    public ItemStack containerStack;
    public boolean cleared;
    public int containerId;

    public ItemStackInventory(Player player, ItemStack containerStack, int inventorySize) {
        this.containerStack = containerStack;
        this.containerItem = containerStack.get(DataComponentsInit.CONTAINER);
        if (this.containerItem == null) {
            this.containerItem = ContainerItem.EMPTY.updateOpen(containerStack, false);
            containerStack.set(DataComponentsInit.CONTAINER, containerItem);
        }
        this.inventorySize = inventorySize;
        if (containerItem.listItem().isEmpty()) {

            ItemStack[] object = new ItemStack[inventorySize];
            Arrays.fill(object, ItemStack.EMPTY);
            List<ItemStack> list = Arrays.asList(object);
            containerItem = containerItem.updateItems(containerStack, list);
        }
        this.inventory = containerItem.listItem();

        this.player = player;
        if (!player.level().isClientSide()) {
            if (containerItem.uid() == 0) {
                containerItem = containerItem.updateUUID(containerStack, IUCore.random.nextInt());
            }
        }

    }

    @Override
    public CustomPacketBuffer writeContainer() {
        return new CustomPacketBuffer(player.registryAccess());
    }

    @Override
    public void readContainer(final CustomPacketBuffer buffer) {

    }

    public List<ItemStack> getInventory() {
        return inventory;
    }


    @Override
    public int getContainerSize() {
        return this.inventory.size();
    }

    public boolean isEmpty() {

        for (ItemStack stack : this.inventory) {
            if (!ModUtils.isEmpty(stack)) {
                return false;
            }
        }

        return true;
    }

    public boolean add(List<ItemStack> stacks, boolean simulate) {
        if (stacks != null && !stacks.isEmpty()) {

            for (ItemStack stack : stacks) {
                for (int i = 0; i < this.inventory.size(); i++) {
                    if (this.get(i) == null || this.get(i).isEmpty()) {
                        if (!simulate) {
                            this.put(i, stack.copy());

                        }
                        return true;
                    } else {
                        if (this.get(i).is(stack.getItem())) {
                            if (this.get(i).getCount() + stack.getCount() <= stack.getMaxStackSize()) {
                                if (stack.getComponents().isEmpty() && this.get(i).getComponents().isEmpty()) {
                                    if (!simulate) {
                                        this.get(i).grow(stack.getCount());
                                    }
                                    return true;
                                } else {
                                    if (stack.getComponents() != null &&
                                            stack.getComponents().equals(this.get(i).getComponents())) {
                                        if (!simulate) {
                                            this.get(i).grow(stack.getCount());

                                        }
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    public ItemStack[] backup() {
        ItemStack[] ret = new ItemStack[this.inventory.size()];

        for (int i = 0; i < this.inventory.size(); ++i) {
            ItemStack content = this.inventory.get(i);
            ret[i] = ModUtils.isEmpty(content) ? ModUtils.emptyStack : content.copy();
        }

        return ret;
    }

    public void restore(ItemStack[] backup) {
        if (backup.length != this.inventory.size()) {
            throw new IllegalArgumentException("invalid array size");
        } else {
            System.arraycopy(backup, 0, this.inventory.toArray(), 0, this.inventory.size());
            this.save();
        }
    }

    public int add(Collection<ItemStack> stacks, boolean simulate) {
        if (stacks != null && !stacks.isEmpty()) {
            ItemStack[] backup = simulate ? this.backup() : null;
            int totalAmount = 0;
            Iterator var5 = stacks.iterator();

            while (true) {
                ItemStack stack;
                int amount;
                do {
                    if (!var5.hasNext()) {
                        if (simulate) {
                            this.restore(backup);
                        }

                        return totalAmount;
                    }

                    stack = (ItemStack) var5.next();
                    amount = ModUtils.getSize(stack);
                } while (amount <= 0);

                label74:
                for (int pass = 0; pass < 2; ++pass) {
                    for (int i = 0; i < this.inventorySize; ++i) {
                        ItemStack existingStack = this.get(i);
                        int space = this.getStackSizeLimit();
                        if (!ModUtils.isEmpty(existingStack)) {
                            space = Math.min(space, existingStack.getMaxStackSize()) - ModUtils.getSize(existingStack);
                        }

                        if (space > 0) {
                            if (pass == 0 && !ModUtils.isEmpty(existingStack) && ModUtils.checkItemEqualityStrict(
                                    stack,
                                    existingStack
                            )) {
                                if (space >= amount) {
                                    existingStack.grow(amount);
                                    this.put(i, existingStack);
                                    amount = 0;
                                    break label74;
                                }
                                existingStack.grow(space);
                                this.put(i, existingStack);
                                amount -= space;
                            } else if (pass == 1 && ModUtils.isEmpty(existingStack)) {
                                if (space >= amount) {
                                    this.put(i, ModUtils.setSize(stack, amount));
                                    amount = 0;
                                    break label74;
                                }

                                this.put(i, ModUtils.setSize(stack, space));
                                amount -= space;
                            }
                        }
                    }
                }

                totalAmount += amount;
            }
        } else {
            return 0;
        }
    }

    public int add(Collection<ItemStack> stacks) {
        return this.add(stacks, false);
    }

    public boolean add(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.add(Collections.singletonList(stack), false);
        }
    }

    public boolean canAdd(Collection<ItemStack> stacks) {
        return this.add(stacks, true) == 0;
    }

    public boolean canAdd(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.add(Collections.singletonList(stack), true);
        }
    }

    public int getStackSizeLimit() {
        return 64;
    }

    public void put(ItemStack content) {
        this.put(0, content);
    }

    public void put(int index, ItemStack content) {
        if (ModUtils.isEmpty(content)) {
            content = ModUtils.emptyStack;
        }

        this.inventory.set(index, content);
        this.save();
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.inventory.get(slot);
    }


    @Override
    public ItemStack removeItem(int index, int amount) {
        ItemStack stack;
        if (index >= 0 && index < this.inventory.size() && !ModUtils.isEmpty(stack = this.inventory.get(index))) {
            ItemStack ret;
            if (amount >= ModUtils.getSize(stack)) {
                ret = stack;
                this.inventory.set(index, ModUtils.emptyStack);
            } else {
                ret = ModUtils.setSize(stack, amount);
                this.inventory.set(index, ModUtils.decSize(stack, amount));
            }

            this.save();
            return ret;
        } else {
            return ModUtils.emptyStack;
        }
    }


    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack ret = this.getItem(index);
        if (!ModUtils.isEmpty(ret)) {
            this.setItem(index, ItemStack.EMPTY);
        }

        return ret;
    }


    @Override
    public void setItem(int slot, ItemStack stack) {
        if (!ModUtils.isEmpty(stack) && ModUtils.getSize(stack) > this.getInventoryStackLimit()) {
            stack = ModUtils.setSize(stack, this.getInventoryStackLimit());
        }

        if (ModUtils.isEmpty(stack)) {
            this.inventory.set(slot, ModUtils.emptyStack);
        } else {
            this.inventory.set(slot, stack);
        }

        this.save();
    }

    @Override
    public boolean canPlaceItem(int p_18952_, ItemStack p_18953_) {
        return true;
    }

    @Override
    public void setChanged() {
        save();
    }


    @Override
    public boolean stillValid(Player player) {
        return player == this.player && this.getPlayerInventoryIndex() >= -1;
    }

    public ItemStack getContainerStack() {
        return containerStack;
    }


    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isItemValidForSlot(int slot, ItemStack stack1) {
        return false;
    }


    public String getName() {
        return "";
    }

    ;

    public Component getDisplayName() {
        return Component.literal(this.getName());
    }

    public boolean isThisContainer(ItemStack stack) {
        if (!ModUtils.isEmpty(stack) && stack.getItem() == this.containerStack.getItem()) {
            return stack.getOrDefault(DataComponentsInit.CONTAINER, ContainerItem.EMPTY).uid() == this.getUid();
        } else {
            return false;
        }
    }

    protected int getUid() {

        return this.containerItem.uid();
    }


    protected int getPlayerInventoryIndex() {
        for (int i = -1; i < this.player.getInventory().getContainerSize(); ++i) {
            ItemStack stack = i == -1 ? this.player.getInventory().getSelected() : this.player.getInventory().getItem(i);
            if (this.isThisContainer(stack)) {
                return i;
            }
        }

        return -2147483648;
    }

    protected void save() {
        if (!player.level().isClientSide()) {
            if (this.containerStack.isEmpty())
                this.containerStack = this.player.getInventory().getSelected();
            if (!this.cleared) {
                boolean dropItself = false;

                for (int i = 0; i < this.inventory.size(); ++i) {
                    if (this.isThisContainer(this.inventory.get(i))) {
                        this.inventory.set(i, ItemStack.EMPTY);
                        dropItself = true;
                    }
                }


                this.containerItem = this.containerItem.updateItems(containerStack, inventory);

                if (dropItself) {
                    this.containerStack = ModUtils.setSize(this.containerStack, 1);
                    ModUtils.dropAsEntity(this.player.level(), this.player.blockPosition(), this.containerStack);
                    this.clear();
                } else {
                    int idx = this.getPlayerInventoryIndex();
                    if (idx < -1) {
                        this.clear();
                    } else if (idx == -1) {
                        this.player.getInventory().setPickedItem(this.containerStack);
                    } else {
                        this.player.getInventory().setItem(idx, this.containerStack);
                    }
                }

            }
        }
        player.containerMenu.broadcastChanges();
    }

    public void saveAsThrown(ItemStack stack) {
        assert !player.level().isClientSide();

        this.containerItem = this.containerItem.updateItems(containerStack, inventory);

        assert containerItem.uid() == 0;

        this.clear();
    }

    public void clear() {
        ItemStack[] object = new ItemStack[inventory.size()];
        Arrays.fill(object, ItemStack.EMPTY);
        List<ItemStack> list = Arrays.asList(object);
        this.inventory = list;
        this.containerItem = this.containerItem.updateItems(containerStack, this.inventory);
        this.cleared = true;
    }

    public void saveAndThrow(ItemStack stack) {
        this.containerItem = this.containerItem.updateItems(containerStack, inventory);
        this.clear();
    }

    @Override
    public ItemStackInventory getParent() {
        return this;
    }


    @Override
    public void addInventorySlot(InvSlot var1) {

    }


    @Override
    public int getBaseIndex(InvSlot var1) {
        return 0;
    }


    @Override
    public MenuType<?> getMenuType() {
        return inventory_container.get();
    }

    @Override
    public int getContainerId() {
        return this.containerId;
    }


    @Override
    public ContainerBase<?> getGuiContainer(Player var1) {
        return null;
    }


    @Override
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> var2) {
        return null;
    }


    @Override
    public void clearContent() {

    }


    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        containerId = p_39954_;
        return getGuiContainer(p_39956_);
    }

    public ItemStack get(int i) {
        return this.inventory.get(i);
    }
}
