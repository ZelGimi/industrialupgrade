package com.denfop.items.energy;

import com.denfop.IUCore;
import com.denfop.container.ContainerBase;
import com.denfop.invslot.Inventory;
import com.denfop.items.ItemStackInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemStackMagnet extends ItemStackInventory {

    public final int inventorySize;
    public final ItemStack itemStack1;
    public final ItemStack[] list;

    public ItemStackMagnet(EntityPlayer player, ItemStack stack, int inventorySize) {
        super(player, stack, inventorySize);
        this.inventorySize = inventorySize;
        this.list = new ItemStack[9];
        Arrays.fill(this.list, ItemStack.EMPTY);
        if (IUCore.proxy.isSimulating()) {
            NBTTagCompound nbt = ModUtils.nbt(containerStack);
            NBTTagList contentList = nbt.getTagList("Items1", 10);

            for (int i = 0; i < contentList.tagCount(); ++i) {
                NBTTagCompound slotNbt = contentList.getCompoundTagAt(i);
                int slot = slotNbt.getByte("Slot");
                if (slot >= 0 && slot < this.list.length) {
                    this.list[slot] = new ItemStack(slotNbt);
                }
            }
        }
        this.itemStack1 = stack;
        this.updatelist();
    }


    public ItemStack getStackInSlot(int slot) {
        return slot >= inventorySize ? this.list[slot - inventorySize] : this.inventory[slot];
    }

    public ItemStack decrStackSize(int index, int amount) {
        if (index < this.inventory.length) {
            ItemStack stack;
            if (index >= 0 && !ModUtils.isEmpty(stack = this.inventory[index])) {
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

    protected void save() {
        super.save();
        if (IUCore.proxy.isSimulating()) {
            if (!this.cleared) {
                boolean dropItself = false;

                for (int i = 0; i < this.list.length; ++i) {
                    if (this.isThisContainer(this.list[i])) {
                        this.list[i] = null;
                        dropItself = true;
                    }
                }

                NBTTagList contentList = new NBTTagList();

                int idx;
                for (idx = 0; idx < this.list.length; ++idx) {
                    if (!ModUtils.isEmpty(this.list[idx])) {
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setByte("Slot", (byte) idx);
                        this.list[idx].writeToNBT(nbt);
                        contentList.appendTag(nbt);
                    }
                }

                ModUtils.nbt(this.containerStack).setTag("Items1", contentList);

                this.containerStack = ModUtils.setSize(this.containerStack, 1);

                if (dropItself) {
                    ModUtils.dropAsEntity(this.player.getEntityWorld(), this.player.getPosition(), this.containerStack);
                    this.clear();
                } else {
                    idx = this.getPlayerInventoryIndex();
                    if (idx < -1) {
                        this.clear();
                    } else if (idx == -1) {
                        this.player.inventory.setItemStack(this.containerStack);
                    } else {
                        this.player.inventory.setInventorySlotContents(idx, this.containerStack);
                    }
                }

            }
        }
    }


    public int getInventoryStackLimit() {
        return 64;
    }

    public ContainerBase<ItemStackMagnet> getGuiContainer(EntityPlayer player) {
        return new ContainerMagnet(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiMagnet(new ContainerMagnet(player, this), itemStack1);
    }


    @Override
    public void addInventorySlot(final Inventory var1) {

    }


    @Nonnull
    public String getName() {
        return "toolbox";
    }

    public boolean hasCustomName() {

        return false;

    }

    public void setInventorySlotContents(int slot, ItemStack stack) {
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

    }

    public ItemStack get(int index) {
        return this.inventory[index];
    }

    public ItemStack[] getAll() {
        return this.inventory;
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

    public void putWithoutSave(int index, ItemStack content) {
        if (ModUtils.isEmpty(content)) {
            content = ModUtils.emptyStack;
        }

        this.inventory[index] = content;
    }

    private boolean add(List<ItemStack> stacks, boolean simulate) {
        final NBTTagCompound nbt = ModUtils.nbt(this.itemStack1);
        boolean white = nbt.getBoolean("white");


        if (stacks != null && !stacks.isEmpty()) {

            for (ItemStack stack : stacks) {
                boolean can = false;
                if (white) {
                    for (ItemStack stack1 : this.list) {
                        if (!stack1.isEmpty() && stack1.isItemEqual(stack)) {
                            can = true;
                            break;
                        }
                    }
                } else {
                    for (ItemStack stack1 : this.list) {
                        if (!stack1.isEmpty() && stack1.isItemEqual(stack)) {
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
                            this.put(i, stack.copy());
                            stack.setCount(0);
                        }
                        return true;
                    } else {
                        if (this.get(i).isItemEqual(stack)) {
                            if (this.get(i).getCount() + stack.getCount() <= stack.getMaxStackSize()) {
                                if (stack.getTagCompound() == null && this.get(i).getTagCompound() == null) {
                                    if (!simulate) {
                                        this.get(i).grow(stack.getCount());
                                        stack.setCount(0);
                                    }
                                    return true;
                                } else {
                                    if (stack.getTagCompound() != null &&
                                            stack.getTagCompound().equals(this.get(i).getTagCompound())) {
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

    private boolean addWithoutSave(List<ItemStack> stacks, boolean simulate) {

        if (stacks != null && !stacks.isEmpty()) {
            final NBTTagCompound nbt = ModUtils.nbt(this.itemStack1);
            boolean white = nbt.getBoolean("white");
            for (ItemStack stack : stacks) {

                boolean can = false;
                if (white) {
                    for (ItemStack stack1 : this.list) {
                        if (!stack1.isEmpty() && stack1.isItemEqual(stack)) {
                            can = true;
                            break;
                        }
                    }
                } else {
                    for (ItemStack stack1 : this.list) {
                        if (!stack1.isEmpty() && stack1.isItemEqual(stack)) {
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
                        if (this.get(i).isItemEqual(stack)) {
                            if (this.get(i).getCount() + stack.getCount() <= stack.getMaxStackSize()) {
                                if (stack.getTagCompound() == null && this.get(i).getTagCompound() == null) {
                                    if (!simulate) {
                                        this.get(i).grow(stack.getCount());
                                        stack.setCount(0);
                                    }
                                    return true;
                                } else {
                                    if (stack.getTagCompound() != null &&
                                            stack.getTagCompound().equals(this.get(i).getTagCompound())) {
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

    public boolean add(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.add(Collections.singletonList(stack), false);
        }
    }

    public boolean addWithoutSave(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.addWithoutSave(Collections.singletonList(stack), false);
        }
    }

    public boolean canAdd(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.add(Collections.singletonList(stack), true);
        }
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        if (itemstack.getItem() instanceof ItemMagnet) {
            return false;
        }
        return !itemstack.isEmpty();
    }

}
