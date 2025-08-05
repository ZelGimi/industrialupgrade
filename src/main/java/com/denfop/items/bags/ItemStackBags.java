package com.denfop.items.bags;

import com.denfop.ElectricItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.container.ContainerBags;
import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiBags;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlot;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.ItemStackInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.*;

public class ItemStackBags extends ItemStackInventory {

    public final int inventorySize;
    public final ItemStack itemStack1;
    public final ItemStack[] list;
    private final double coef;


    public ItemStackBags(Player player, ItemStack stack, int inventorySize) {
        super(player, stack, inventorySize);
        this.list = new ItemStack[9];
        Arrays.fill(this.list, ItemStack.EMPTY);
        this.inventorySize = inventorySize;
        this.itemStack1 = stack;
        if (!player.getLevel().isClientSide()) {
            CompoundTag nbt = ModUtils.nbt(containerStack);
            ListTag contentList = nbt.getList("Items1", 10);

            for (int i = 0; i < contentList.size(); ++i) {
                CompoundTag slotNbt = contentList.getCompound(i);
                int slot = slotNbt.getByte("Slot");
                if (slot >= 0 && slot < this.list.length) {
                    this.list[slot] = ItemStack.of(slotNbt);
                }
            }
        }
        this.coef = 1D - (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, stack).number * 0.25D : 0);

    }

    @Override
    public ItemStack getItem(int slot) {
        return slot >= inventorySize ? this.list[slot - inventorySize] : this.inventory[slot];

    }

    public void save() {
        super.save();
        if (!this.player.getLevel().isClientSide) {
            if (!this.cleared) {
                boolean dropItself = false;

                for (int i = 0; i < this.list.length; ++i) {
                    if (this.isThisContainer(this.list[i])) {
                        this.list[i] = ItemStack.EMPTY;
                        dropItself = true;
                    }
                }

                ListTag contentList = new ListTag();

                int idx;
                for (idx = 0; idx < this.list.length; ++idx) {
                    if (!ModUtils.isEmpty(this.list[idx])) {
                        CompoundTag nbt = new CompoundTag();
                        nbt.putByte("Slot", (byte) idx);
                        this.list[idx].save(nbt);
                        contentList.add(nbt);
                    }
                }

                ModUtils.nbt(this.containerStack).put("Items1", contentList);

                this.containerStack = ModUtils.setSize(this.containerStack, 1);

                if (dropItself) {
                    ModUtils.dropAsEntity(this.player.getLevel(), this.player.blockPosition(), this.containerStack);
                    this.clear();
                } else {
                    idx = this.getPlayerInventoryIndex();
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
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot < this.inventory.length) {
            if (!ModUtils.isEmpty(stack) && ModUtils.getSize(stack) > this.getInventoryStackLimit()) {
                stack = ModUtils.setSize(stack, this.getInventoryStackLimit());
            }

            if (ModUtils.isEmpty(stack)) {
                this.inventory[slot] = ModUtils.emptyStack;
            } else {
                this.inventory[slot] = stack;
            }
        } else {
            if (!ModUtils.isEmpty(stack) && ModUtils.getSize(stack) > this.getInventoryStackLimit()) {
                stack = ModUtils.setSize(stack, this.getInventoryStackLimit());
            }

            if (ModUtils.isEmpty(stack)) {
                this.list[slot - this.inventory.length] = ModUtils.emptyStack;
            } else {
                this.list[slot - this.inventory.length] = stack;
            }
        }
        this.updatelist();
        this.save();
    }

    private void updatelist() {
        List<BagsDescription> list = new ArrayList<>();
        for (ItemStack stack : this.inventory) {
            if (stack == null) {
                continue;
            }
            if (stack.isEmpty()) {
                continue;
            }
            if (list.isEmpty()) {
                list.add(new BagsDescription(stack));
            } else if (list.contains(new BagsDescription(stack))) {
                list.get(list.indexOf(new BagsDescription(stack))).addCount(stack.getCount());
            } else {
                list.add(new BagsDescription(stack));
            }
        }
        final CompoundTag nbt = ModUtils.nbt(containerStack);
        CompoundTag nbt1 = new CompoundTag();
        nbt1.putInt("size", list.size());
        for (int i = 0; i < list.size(); i++) {
            nbt1.put(String.valueOf(i), list.get(i).write(new CompoundTag()));
        }
        nbt.put("bag", nbt1);
    }

    public void saveAndThrow(ItemStack stack) {
        ListTag contentList = new ListTag();

        for (int i = 0; i < this.inventory.length; ++i) {
            if (!ModUtils.isEmpty(this.inventory[i])) {
                CompoundTag nbt = new CompoundTag();
                nbt.putByte("Slot", (byte) i);
                this.inventory[i].save(nbt);
                contentList.add(nbt);
            }
        }

        ModUtils.nbt(stack).put("Items", contentList);
        this.clear();
    }


    private boolean addWithoutSave(List<ItemStack> stacks, boolean simulate) {

        if (stacks != null && !stacks.isEmpty()) {
            final CompoundTag nbt = ModUtils.nbt(this.itemStack1);
            boolean white = nbt.getBoolean("white");
            for (ItemStack stack : stacks) {

                boolean can = false;
                if (white) {
                    for (ItemStack stack1 : this.list) {
                        if (!stack1.isEmpty() && stack1.is(stack.getItem())) {
                            can = true;
                            break;
                        }
                    }
                } else {
                    for (ItemStack stack1 : this.list) {
                        if (!stack1.isEmpty() && stack1.is(stack.getItem())) {
                            can = false;
                            break;
                        } else {
                            can = true;
                        }
                    }
                }
                if (!can) {
                    return false;
                }
                for (int i = 0; i < this.inventory.length; i++) {
                    if (this.get(i) == null || this.get(i).isEmpty()) {
                        if (!simulate) {
                            this.putWithoutSave(i, stack.copy());
                            stack.setCount(0);
                        }
                        return true;
                    } else {
                        if (this.get(i).is(stack.getItem())) {
                            if (this.get(i).getCount() + stack.getCount() <= stack.getMaxStackSize()) {
                                if (stack.getTag() == null && this.get(i).getTag() == null) {
                                    if (!simulate) {
                                        this.get(i).grow(stack.getCount());
                                        stack.setCount(0);
                                    }
                                    return true;
                                } else {
                                    if (stack.getTag() != null &&
                                            stack.getTag().equals(this.get(i).getTag())) {
                                        if (!simulate) {
                                            this.get(i).grow(stack.getCount());
                                            stack.setCount(0);

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


    public boolean addWithoutSave(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.addWithoutSave(Collections.singletonList(stack), false);
        }
    }


    public void putWithoutSave(int index, ItemStack content) {
        if (ModUtils.isEmpty(content)) {
            content = ModUtils.emptyStack;
        }

        this.inventory[index] = content;
    }

    @Override
    public ItemStack removeItem(int index, int amount) {
        if (index < this.inventory.length) {
            ItemStack stack;
            if (index >= 0 && index < this.inventory.length && !ModUtils.isEmpty(stack = this.inventory[index])) {
                ItemStack ret;
                if (amount >= ModUtils.getSize(stack)) {
                    ret = stack;
                    this.inventory[index] = ModUtils.emptyStack;
                } else {
                    ret = ModUtils.setSize(stack, amount);
                    this.inventory[index] = ModUtils.decSize(stack, amount);
                }

                this.save();
                return ret;
            } else {
                return ModUtils.emptyStack;
            }
        } else {
            ItemStack stack;
            if (index - this.inventory.length < list.length && !ModUtils.isEmpty(stack =
                    this.list[index - this.inventory.length])) {
                ItemStack ret;
                if (amount >= ModUtils.getSize(stack)) {
                    ret = stack;
                    this.list[index - this.inventory.length] = ModUtils.emptyStack;
                } else {
                    ret = ModUtils.setSize(stack, amount);
                    this.list[index - this.inventory.length] = ModUtils.decSize(stack, amount);
                }

                this.save();
                return ret;
            } else {
                return ModUtils.emptyStack;
            }
        }
    }

    @Override
    public ItemStackInventory getParent() {
        return this;
    }


    @Override
    public void addInventorySlot(final InvSlot var1) {

    }

    @Override
    public int getBaseIndex(final InvSlot var1) {
        return 0;
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public GuiCore<ContainerBase<?>> getGui(Player var1, ContainerBase<?> menu) {
        ContainerBags containerLeadBox = (ContainerBags) menu;
        return new GuiBags(containerLeadBox, this.itemStack1);
    }

    public ContainerBags getGuiContainer(Player player) {
        return new ContainerBags(player, this);
    }


    public ItemStack get(int index) {
        return this.inventory[index];
    }

    @Override
    public boolean canPlaceItem(int p_18952_, ItemStack itemstack) {
        if (itemstack.getItem() instanceof ItemEnergyBags) {
            return false;
        }
        final CompoundTag nbt = ModUtils.nbt(this.itemStack1);
        boolean white = nbt.getBoolean("white");
        boolean can = false;
        if (white) {
            for (ItemStack stack1 : this.list) {
                if (!stack1.isEmpty() && stack1.is(itemstack.getItem())) {
                    can = true;
                    break;
                }
            }
        } else {
            for (ItemStack stack1 : this.list) {
                if (!stack1.isEmpty() && stack1.is(itemstack.getItem())) {
                    can = false;
                    break;
                } else {
                    can = true;
                }
            }
        }
        if (!can) {
            return false;
        }

        if (ElectricItem.manager.canUse(itemStack1, 50 * coef)) {
            return !itemstack.isEmpty();
        } else {
            return false;
        }
    }

    protected void restore(ItemStack[] backup) {
        if (backup.length != this.inventory.length) {
            throw new IllegalArgumentException("invalid array size");
        } else {
            System.arraycopy(backup, 0, this.inventory, 0, this.inventory.length);

        }
    }

    @Nonnull
    public String getName() {
        return "toolbox";
    }

    public boolean hasCustomName() {
        return false;
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

    protected ItemStack[] backup() {
        ItemStack[] ret = new ItemStack[this.inventory.length];

        for (int i = 0; i < this.inventory.length; ++i) {
            ItemStack content = this.inventory[i];
            ret[i] = ModUtils.isEmpty(content) ? ModUtils.emptyStack : content.copy();
        }

        return ret;
    }

    public void put(ItemStack content) {
        this.put(0, content);
    }

    public void put(int index, ItemStack content) {
        if (ModUtils.isEmpty(content)) {
            content = ModUtils.emptyStack;
        }

        this.inventory[index] = content;
        this.save();
    }

    public int getStackSizeLimit() {
        return 64;
    }

    private boolean add(List<ItemStack> stacks, boolean simulate) {
        if (stacks != null && !stacks.isEmpty()) {

            for (ItemStack stack : stacks) {
                for (int i = 0; i < this.inventory.length; i++) {
                    if (this.get(i) == null || this.get(i).isEmpty()) {
                        if (!simulate) {
                            this.put(i, stack.copy());

                        }
                        return true;
                    } else {
                        if (this.get(i).is(stack.getItem())) {
                            if (this.get(i).getCount() + stack.getCount() <= stack.getMaxStackSize()) {
                                if (stack.getTag() == null && this.get(i).getTag() == null) {
                                    if (!simulate) {
                                        this.get(i).grow(stack.getCount());
                                    }
                                    return true;
                                } else {
                                    if (stack.getTag() != null &&
                                            stack.getTag().equals(this.get(i).getTag())) {
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

    private int add(Collection<ItemStack> stacks, boolean simulate) {
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

}
